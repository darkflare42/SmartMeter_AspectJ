package Aspects;
import Engine.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * There are a lot of places in the code which deal with disconnecting/specifically changing the status of a meter
 * to INACTIVE. This aspect deals with these situations
 */
public aspect DisconnectMeterAspect {

    private static final int MAX_CITY_WATTAGE = 10000;
    private static final int CUTOFF_DATE = 120;

    /**
     * This pointcut is when a client has disconnected from the company, we have to delete all his
     * meter's from the DB
     */
    pointcut ClientDisconnected(Customer c) : execution(* Engine.DBComm.deleteCustomer(Customer)) && args(c);

    /**
     * This pointcut should happen after calling to the function which returns all the customers
     * Who have not payed their bill
     */
    pointcut ClientBillNotPayed() : execution(* Engine.BillingEngine.checkMonthlyBilling());

    /**
     * When a meter reaches his max wattage then we need to deal with it - in this case setting the meter
     * to INACTIVE
     */
    pointcut MeterWattageMaxed() : execution(* Engine.PowerMeter.readWattage());

    /**
     * When a region is overloaded, we need to inactivate all meters
     */
    pointcut MeterWattageOverload(): execution(* Engine.MeterCommunication.readAllMeters());

    /**
     * This advice should check the wattage returned and if the wattage is over the max wattage, the meter
     * should be Disconnected (i.e set to INACTIVE)
     */
    after() returning(int watt) : MeterWattageMaxed(){
        PowerMeter currMeter =((PowerMeter)thisJoinPoint.getTarget());
        int maxWattage = currMeter.getMaxWattage();
        if(watt >= maxWattage){
            currMeter.setInactive();
            DBComm.updateMeter(currMeter);
        }
    }

    /**
     * This advice occurs before a client is removed from the DB. We need to calculate the current bill,
     * get all the meters he has and then as delete them from the DB
     */
    before(Customer c) : ClientDisconnected(c){
        BillingEngine.calculateCurrentBill(c);
        LinkedList<PowerMeter> userMeters = DBComm.getAllMeterdByUserId(c.getID());
        for(PowerMeter m : userMeters){
            DBComm.deletePowerMeter(m);
        }
    }

    /**
     * This advice occurs after all the meters have been read.
     * It goes through each city, and checks if the city's power is overloaded
     * if it is - it sets all the meters to INACTIVE
     */
    after() : MeterWattageOverload(){
        LinkedList<String> cities = DBComm.getAllCities();
        for(String city : cities){
            checkAndHandleOverload(city);
        }
    }


    /**
     * A helper function for checking and handling if a city is overloaded
     * This is for testing purposes only, and is not reflective of how or where the function is located in the
     * real life system
     * @param city The city to check the overloaded status
     */
    private void checkAndHandleOverload(String city){
        int cityWattage = 0;
        List<PowerMeter> cityActiveMeters = DBComm.getAllMetersByCity(city);
        for(PowerMeter m : cityActiveMeters){
            cityWattage += m.getCurrentWattage();
        }

        if(cityWattage >= MAX_CITY_WATTAGE){
            cityActiveMeters.forEach(PowerMeter::setInactive);
        }
    }

    /**
     * This Advice occurs after a successful return of the function which returns all the customers
     * who have not payed their monthly bill. Their meters need to be set as INACTIVE
     */
    after() : ClientBillNotPayed(){
        Calendar currDate = new GregorianCalendar();
        LinkedList<Bill> allBills = DBComm.getAllBills();
        for(Bill b : allBills){
            if(b.getPayed()) continue;
            Date cutOffDate = b.getCutoffDate();
            long daysDiff =  getDateDiff(currDate.getTime(), cutOffDate, TimeUnit.DAYS);
            if(daysDiff >= CUTOFF_DATE){ //We check if the bill has passed the maximum number of cutoff days
                disconnectAllMeters(b.getCustomer());
            }
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

    /**
     * A helper function that disconnects all of a client's meters
     * @param c The customer's meters
     */
    public static void disconnectAllMeters(Customer c){
        LinkedList<PowerMeter> meters = DBComm.getAllMeterdByUserId(c.getID());
        for(PowerMeter m : meters){
            m.setInactive();
        }
    }









}
