package Aspects;
import Engine.*;
import Engine.PowerMeter;

/**
 * Created by Or Keren on 21/05/2016.
 * This aspect is to allow the Meter communicator to be updated on the status change of a meter in the system
 */
public aspect MeterStatusChangedAspect {

    pointcut meterAdded(PowerMeter m) : execution(* Engine.DBComm.addNewMeter(Engine.PowerMeter)) && args(m);

    pointcut meterActiveInactive() : execution(* Engine.PowerMeter.setActive()) || execution(* Engine.PowerMeter.setInactive());

    pointcut meterRemoved(PowerMeter m) : call(* Engine.DBComm.deletePowerMeter(PowerMeter)) && args(m);

    /**
     * After the meter is added we need to add the meter to the Communicator's internal list
     */
    after(PowerMeter m): meterAdded(m){
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
    after() : meterActiveInactive(){
        PowerMeter m = (PowerMeter)thisJoinPoint.getTarget();
        switch(m.getStatus()){
            case ACTIVE: //Means we need to add the meter to the active meters inside the communicator
                MainTest.communicator.addMeter(m);

                break;
            case INACTIVE: //Means we need to remove the meter from the meter's list inside the communicator
                MainTest.communicator.removeMeter(m);
                break;
        }
        DBComm.updateMeter(m);
    }

}

