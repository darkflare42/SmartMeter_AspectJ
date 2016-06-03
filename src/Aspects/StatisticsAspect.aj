package Aspects;
import Engine.Customer;
import Engine.PowerMeter;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.io.*;


/**
 * This is a spectative aspect which handles certain statistics gathering for the system
 */
public aspect StatisticsAspect {

    //Local variables for saving information
    private static Map<Integer, Integer> userDisconnects = new HashMap<Integer, Integer>();
    private static Map<Integer, Integer> meterStatusChanges = new HashMap<Integer, Integer>();


    /**
     * This pointcut occurs when the system is shutdown - we save the statistics to a local file
     */
    pointcut SaveStatistics() : execution(* MainTest.shutdown());

    /**
     * This pointcut occurs when a customer's meters are disconnected. We would like to count the times that
     * each customer has been disconnected during the runtime of the system
     */
    pointcut CustomerDisconnected(Customer c) : execution(* DisconnectMeterAspect.disconnectAllMeters(Customer)) &&
                                                args(c);

    /**
     * This pointcut occurs when a meter's status has changed. We would like to monitor and count the number of times
     * each meter has been activated or de-activated
     */
    pointcut MeterStatusChanged() : execution(* Engine.PowerMeter.setActive()) ||
                                    execution(* Engine.PowerMeter.setInactive());

    /**
     * This advice occurs before the shutdown, and in this case saves the two maps into two files
     */
    before() : SaveStatistics(){
        //Save statistic members to local file/DB
        try
        {
            FileOutputStream fosDisconnects =
                    new FileOutputStream("userDisconnects.pgds");
            FileOutputStream fosMeterStatusChanges =
                    new FileOutputStream("MeterStatusChanges.pgds");

            ObjectOutputStream oosDisconnects = new ObjectOutputStream(fosDisconnects);
            ObjectOutputStream oosStatusChanges = new ObjectOutputStream(fosMeterStatusChanges);

            oosDisconnects.writeObject(userDisconnects);
            oosStatusChanges.writeObject(meterStatusChanges);

            oosDisconnects.close();
            oosStatusChanges.close();
            fosDisconnects.close();
            fosDisconnects.close();
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    /**
     * This advice occurs after the function has been called, and updates the value in the map
     */
    after(Customer c) : CustomerDisconnected(c){
        Integer value = userDisconnects.get(c.getID());
        if(value == null){ //Then the user is not in the map, so we add
            userDisconnects.put(c.getID(), 1);
        }
        else{
            userDisconnects.put(c.getID(), value+1);
        }
    }

    /**
     * * This advice occurs after the function has been called, and updates the value in the map
     */
    after() :  MeterStatusChanged(){
        PowerMeter m = (PowerMeter)thisJoinPoint.getTarget();
        Integer value = meterStatusChanges.get(m.getID());
        if(value == null){
            meterStatusChanges.put(m.getID(), 1);
        }
        else{
            meterStatusChanges.put(m.getID(), value+1);
        }
    }

}
