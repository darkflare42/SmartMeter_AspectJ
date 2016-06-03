package Aspects;
import Engine.Customer;
import Engine.PowerMeter;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Or Keren on 21/05/2016.
 */
public aspect StatisticsAspect {

    private static Map<Integer, Integer> userDisconnects = new HashMap<Integer, Integer>();
    private static Map<Integer, Integer> meterStatusChanges = new HashMap<Integer, Integer>();


    pointcut SaveStatistics() : execution(* MainTest.shutdown());

    pointcut CustomerDisconnected(Customer c) : execution(* DisconnectMeterAspect.disconnectAllMeters(Customer)) && args(c);

    pointcut MeterStatusChanged() : execution(* Engine.PowerMeter.setActive()) || execution(* Engine.PowerMeter.setInactive());

    before() : SaveStatistics(){
        //Save statistic members to local file/DB
    }

    after(Customer c) : CustomerDisconnected(c){
        Integer value = userDisconnects.get(c.getID());
        if(value == null){ //Then the user is not in the map, so we add
            userDisconnects.put(c.getID(), 1);
        }
        else{
            userDisconnects.put(c.getID(), value+1);
        }
    }


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
