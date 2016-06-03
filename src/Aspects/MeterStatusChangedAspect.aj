package Aspects;
import Engine.*;
import Engine.PowerMeter;

/**
 * This aspect is to allow the Meter communicator to be updated on the status change of a meter in the system
 * This can also be used for other logic, such as logging or statistics (any spectative aspect)
 */
public aspect MeterStatusChangedAspect {

    /**
     * This pointcut deals with the situation that a meter has been added to the system
     */
    pointcut MeterAdded(PowerMeter m) : execution(* Engine.DBComm.addNewMeter(Engine.PowerMeter)) && args(m);

    /**
     * This pointcut deals with the situation that a meter has been removed from the system
     */
    pointcut meterRemoved(PowerMeter m) : call(* Engine.DBComm.deletePowerMeter(PowerMeter)) && args(m);

    /**
     * This pointcut deals with the situation that a meter's status has been changed
     */
    pointcut MeterActiveInactive() : execution(* Engine.PowerMeter.setActive()) ||
                                        execution(* Engine.PowerMeter.setInactive());



    /**
     * After the meter is added we need to add the meter to the Communicator's internal list
     */
    after(PowerMeter m): MeterAdded(m){
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
    after() : MeterActiveInactive(){
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

