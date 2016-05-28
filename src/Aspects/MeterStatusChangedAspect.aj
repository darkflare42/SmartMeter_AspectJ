package Aspects;
import Engine.*;

/**
 * Created by Or Keren on 21/05/2016.
 * This aspect is to allow the Meter communicator to be updated on the status change of a meter in the system
 */
public aspect MeterStatusChangedAspect {

    pointcut meterAdded(PowerMeter m) : execution(* Engine.DBComm.addNewMeter(Engine.PowerMeter)) && args(m);

    pointcut meterActiveInactive(PowerMeter m, EngineConsts.MeterStatus status) :
        call(* Engine.DBComm.setActiveStatus(PowerMeter, EngineConsts.MeterStatus)) && args(m, status); //TODO: Add this function to the DBComm

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



    pointcut MeterAdded(PowerMeter m) : execution(* DBComm.addNewMeter(PowerMeter)) && args(m);

    pointcut MeterActiveInactive(PowerMeter m, EngineConsts.MeterStatus status) :
            call(* Engine.DBComm.setActiveStatus(PowerMeter, EngineConsts.MeterStatus)) && args(m, status);

    pointcut MeterRemoved(PowerMeter m) : execution(* DBComm.deletePowerMeter(PowerMeter)) && args(m);

    after(PowerMeter m): MeterAdded(m){
        MainTest.communicator.addMeter(m);
    }

    after(PowerMeter m): MeterRemoved(m){
        MainTest.communicator.removeMeter(m);
    }

    after(PowerMeter m, EngineConsts.MeterStatus status) : MeterActiveInactive(m, status){
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

