package Aspects;


/**
 * Created by Or Keren on 21/05/2016.
 * This aspect is to allow the Meter communicator to be updated on the status change of a meter in the system
 */
public aspect MeterStatusChangedAspect {

    pointcut meterAdded();

    pointcut meterActiveInactive();

    pointcut meterRemoved();

    public void test(){

    }
}
