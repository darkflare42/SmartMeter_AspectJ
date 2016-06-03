package Aspects;

import Engine.DBComm;
import Engine.PowerMeter;
import Engine.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 *  This aspect deals with updating the current billing of all the customers in the system
 *
 * Created by Or Keren on 21/05/2016.
 */
public aspect UpdateCurrentBillAspect {

    private static final int FINE_DAYS = 90;
    private static final int DEFAULT_FINE = 200;

    /**
     * Whenever a meter's reading is changed, the value inside the billing table should be updated to reflect this
     */
    pointcut MeterReadingChanged() : execution(* Engine.PowerMeter.readWattage());

    /**
     * Whenever a country's tariff changes - the
     */
    pointcut CountryTariffChanged(String country, int newTariff) :
                                 execution(* Engine.DBComm.updateCountryTariff(String, int)) &&
                                 args(country, newTariff);

    /**
     * This pointcut should change the billing information if the customer has not payed his bill up to certain points
     */
    pointcut FineCustomer() : execution(* Engine.BillingEngine.checkMonthlyBilling());

    pointcut CityTariffChanged(String city, int newTariff) :
                                execution(* Engine.DBComm.updateCityTariff(String, int)) &&
                                args(city, newTariff);


    /**
     * This advice should occur after every reading of the meter's wattage, it should go to the DB and update
     * the current meter's owner's bill
     */
    after() returning(int watt) : MeterReadingChanged(){
        PowerMeter currMeter =((PowerMeter)thisJoinPoint.getTarget());

        BillingEngine.updateCurrentCustomerBill(currMeter.getCustomerID(), watt);

    }

    /**
     * This advice occurs every month. We go over every single bill that has not been payed and is less than 3 months
     * from the cutoff date. For every month that the customer has not payed-  we add a fine to his previous bill
     */
    after() : FineCustomer(){
        Calendar currDate = new GregorianCalendar();
        LinkedList<Bill> allBills = DBComm.getAllBills();
        for(Bill b : allBills){
            if(b.getPayed()) continue;
            Date cutOffDate = b.getCutoffDate();
            long daysDiff =  getDateDiff(currDate.getTime(), cutOffDate, TimeUnit.DAYS);
            if(daysDiff >= FINE_DAYS){
                b.updateFine(DEFAULT_FINE);
                DBComm.updateBill(b);
            }
        }

    }

    private int calculateDateDifference(Calendar currDate, Calendar issuedDate){
        int diffYear = currDate.get(Calendar.YEAR) - issuedDate.get(Calendar.YEAR);
        return diffYear * 12 + currDate.get(Calendar.MONTH) - issuedDate.get(Calendar.MONTH);
    }

    /**
     * This advice occurs after chaning a country's tariff. We go through all of the current active bills and update
     * the tariff according to the new value
     */
    after(String country, int newTariff) :  CountryTariffChanged(country, newTariff){
        LinkedList<Customer> customers = DBComm.getCustomersFromCountry(country);
        updateCountryTariffForCustomers(customers, newTariff);
    }

    /**
     * This advice occurs after chaning a city's's tariff. We go through all of the current active bills and update
     * the tariff according to the new value
     */
    after(String city, int newTariff) :  CityTariffChanged(city, newTariff){
        LinkedList<Customer> customers = DBComm.getCustomersFromCity("IL", city);
        updateCityTariffForCustomers(customers, newTariff);
    }

    private void updateCityTariffForCustomers(LinkedList<Customer> customers, int newTariff){
        for(Customer c : customers){
            Bill b = DBComm.getBillByCustomerID(c.getID());
            b.updateCityTariff(newTariff);
            DBComm.updateBill(b);
        }
    }

    private void updateCountryTariffForCustomers(LinkedList<Customer> customers, int newTariff){
        for(Customer c: customers){
            Bill b = DBComm.getBillByCustomerID(c.getID());
            b.updateCountryTariff(newTariff);
            DBComm.updateBill(b);
        }
    }
    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }






}
