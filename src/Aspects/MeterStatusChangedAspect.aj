package Aspects;
import Engine.*;

/**
 * Created by Or Keren on 21/05/2016.
 * This aspect is to allow the Meter communicator to be updated on the status change of a meter in the system
 */
public aspect MeterStatusChangedAspect {

    pointcut meterAdded(PowerMeter m, Customer c) : call(public static void Engine.DBComm.addNewMeter(m, c));

    pointcut meterActiveInactive(PowerMeter m, EngineConsts.MeterStatus status) :
        call(public static void Engine.DBComm.setActiveStatus(m, status)) //TODO: Add this funtion to the DBComm

    pointcut meterRemoved(PowerMeter m) : call(public static void Engine.DBComm.deltePowerMeter(m));

    /**
     * After the meter is added we need to add the meter to the Communicator's internal list
     */
    after(PowerMeter m, Customer c): meterAdded(m, c){
        MainTest.communicator.addMeter(m);
    }


    /**
     * After the meter is removed, we need to remove the meter from the communicator's internal list
     */
    after(PowerMeter m): meterRemoved(m){
        MainTest.communicator.removeMeter(m);
    }


    /**
     * After the active status of the meter has changed, we need to deal with it- i.e remove or add it to the
     * communicator's list
     */
    after(PowerMeter m, EngineConsts.MeterStatus status) : meterActiveInactive(m, status){
        switch(status){
            case ACTIVE: //Means we need to add the meter to the active meters inside the communicator
                MainTest.communicator.addMeter(m);
                break;
            case INACTIVE: //Means we need to remove the meter from the meter's list inside the communicator
                MainTest.communicator.removeMeter(m);
                break;
        }
    }
}
