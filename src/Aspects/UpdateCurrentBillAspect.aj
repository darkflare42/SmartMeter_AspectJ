//package Aspects;
//
//import Engine.DBComm;
//import Engine.PowerMeter;
//import Engine.*;
//
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.LinkedList;
//
///**
// *  This aspect deals with updating the current billing of all the customers in the system
// *
// * Created by Or Keren on 21/05/2016.
// */
//public aspect UpdateCurrentBillAspect {
//
//    /**
//     * Whenever a meter's reading is changed, the value inside the billing table should be updated to reflect this
//     */
//    pointcut MeterReadingChanged() : execution(* Engine.PowerMeter.readWattage());
//
//    /**
//     * Whenever a country's tariff changes - the
//     */
//    pointcut CountryTariffChanged(int countryID, int newTariff) : execution(* Engine.DBComm.changeCountryTariff(int, int)) && args(countryID, newTariff); //TODO: Add to DBComm
//
//    /**
//     * This pointcut should change the billing information if the customer has not payed his bill up to certain points
//     */
//    pointcut FineCustomer() : execution(* Engine.BillingEngine.checkMonthlyBilling())
//
//    pointcut CityTariffChanged(int countryID, int cityID, int newTariff) : execution(* Engine.DBComm.changeCityTariff(int, int, int)) && args(countryID, cityID, newTariff); //TODO: Add to DBComm
//
//
//    /**
//     * This advice should occur after every reading of the meter's wattage, it should go to the DB and update
//     * the current meter's owner's bill
//     */
//    after() returning(int watt) : MeterReadingChanged(){
//        PowerMeter currMeter =((PowerMeter)thisJoinPoint.getTarget());
//
//        DBComm.updateUserBill(currMeter.getCustomerID(), watt); //TODO: Add the function to DBComm
//
//    }
//
//    /**
//     * This advice occurs every month. We go over every single bill that has not been payed and is less than 3 months
//     * from the cutoff date. For every month that the customer has not payed-  we add a fine to his previous bill
//     */
//    after() : FineCustomer(){
//        LinkedList<Customer> customers = DBComm.getCustomersNotPayed(); //TODO: Add to DB. Should return customers that have not payed but still have time to pay until cutoff
//
//        Calendar currDate = new GregorianCalendar();
//        Calendar issuedDate;
//        for(Customer c : customers){
//            LinkedList<Bill> finedBills = DBComm.getFinedBillsByCustomer(c); //TODO: Add to DBComm, should only return bills up until cutoff
//            for(Bill b : finedBills){
//                issuedDate = new GregorianCalendar();
//                issuedDate.setTime(b.getIssuedDate());
//                int dateDiff = calculateDateDifference(currDate, issuedDate);
//                b.updateFine(dateDiff * 10); //Arbitrary value
//            }
//
//        }
//
//
//    }
//
//    private int calculateDateDifference(Calendar currDate, Calendar issuedDate){
//        int diffYear = currDate.get(Calendar.YEAR) - issuedDate.get(Calendar.YEAR);
//        return diffYear * 12 + currDate.get(Calendar.MONTH) - issuedDate.get(Calendar.MONTH);
//    }
//
//    /**
//     * This advice occurs after chaning a country's tariff. We go through all of the current active bills and update
//     * the tariff according to the new value
//     */
//    after(int countryID, int newTariff) :  CountryTariffChanged(countryID, newTariff){
//        LinkedList<Customer> customers = DBComm.getCustomersFromCountry(countryID); //TODO: Add to DBComm
//        updateTariffForCustomers(customers, newTariff);
//    }
//
//    /**
//     * This advice occurs after chaning a city's's tariff. We go through all of the current active bills and update
//     * the tariff according to the new value
//     */
//    after(int countryID, int cityID, int newTariff) :  CityTariffChanged(countryID, cityID, newTariff){
//        LinkedList<Customer> customers = DBComm.getCustomersFromCity(countryID, cityID); //TODO: Add to DBComm
//        updateTariffForCustomers(customers, newTariff);
//    }
//
//    private void updateTariffForCustomers(LinkedList<Customer> customers, int newTariff){
//        for(Customer c : customers){
//            DBComm.updateCustomerCurrentBill(c, newTariff);
//        }
//    }
//
//}
