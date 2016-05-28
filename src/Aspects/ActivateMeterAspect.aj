package Aspects;
import Engine.*;

/**
 * This aspect deals with activating a meter (setting the status to ACTIVE)
 * Created by Or Keren on 25/05/2016.
 */
public aspect ActivateMeterAspect {

    pointcut ClientLateBillPayed(Customer c, Bill b) : execution(* Engine.BillingEngine.PayBill(Customer, Bill))
                                                        && args(c, b);

    pointcut CityOverloadFixed(int cityID) : execution(* Communicator.overloadFixed(int)) && args(cityID);


    /**
     * This pointcut is for reactivating meters that have max wattage set - we need to reset the wattage reading
     * and also activate meters that have been inactivated because they reached max wattage
     */
    pointcut ReactivatePrepaid() : execution(* BillingEngine.FirstOfMonth());

    pointcut MeterMaxWattageIncreased(int newWattage) : execution(* PowerMeter.setMaxWattage(int)) && args(newWattage);





}
