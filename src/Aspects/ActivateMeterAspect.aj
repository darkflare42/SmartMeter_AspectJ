package Aspects;
import Engine.*;

import java.util.LinkedList;

/**
 * This aspect deals with the concern of activating a meter
 */
public aspect ActivateMeterAspect {

    /**
     * This pointcut is executed when a customer pays his bill
     */
    pointcut ClientLateBillPayed(Customer c, Bill b) : execution(* Engine.BillingEngine.PayBill(Customer, Bill))
                                                        && args(c, b);

    /**
     * This pointcut occurs when a system/grid administrator initiates a "overload fixed" event
     * This occurs after an electric shutdown of a certain region/city/country has occurred and is now fixed
     */
    pointcut CityOverloadFixed(String city) : execution(* Engine.MeterCommunication.overloadFixed(String)) && args(city);

    /**
     * This pointcut is for reactivating meters that have max wattage set - we need to reset the wattage reading
     * and also activate meters that have been inactivated because they reached max wattage
     */
    pointcut ReactivatePrepaid() : execution(* BillingEngine.checkMonthlyBilling());


    /**
     * This pointcut is for when a power meter's max wattage has been changed. In this case, for simplicity, we
     * take into account that it can only be set to a higher value.
     */
    pointcut MeterMaxWattageIncreased(int newWattage,PowerMeter pm) : execution(* PowerMeter.setMaxWattage(int)) &&
            args(newWattage) && this(pm);



    /**
     * The advice for the pointcut goes over all the client's meters and activates them.
     * This makes sure that if a user's meters have been disconnected because of lack of payment for example, then
     * after he finishes his payment, the meters are active
     */
    after(Customer c, Bill b) : ClientLateBillPayed(c,b){
        LinkedList<PowerMeter> allMeters = DBComm.getAllMeterdByUserId(c.getID());
        for(int i=0;i<allMeters.size();i++){
            allMeters.get(i).setActive();
        }
    }

    /**
     * This advice takes all the meters in a certain city, and activates them
     */
    after(String city) : CityOverloadFixed(city){
        LinkedList<PowerMeter> allMeters = DBComm.getAllMeters();
        for(int i=0;i<allMeters.size();i++){
            int id = allMeters.get(i).getCustomerID();
            Customer us =DBComm.getCustumerById(id);
            if(us.getAddress().getCity().equals(city)){
                allMeters.get(i).setActive();
            }
        }
    }

    /**
     * The advice occurs after the function has been called. We go over all meter's that are prepaid (have a max
     * wattage of value different than -1), reset their current reading and set them as active
     */
    after(): ReactivatePrepaid(){
        LinkedList<PowerMeter> allMeters = DBComm.getAllMeters();
        for(int i=0;i<allMeters.size();i++){
            if(allMeters.get(i).getMaxWattage()!= -1){
                allMeters.get(i).setM_currWattageReading(0);
                allMeters.get(i).setActive();
            }
            allMeters.get(i).setActive();
        }
    }

    /**
     * In this simple advice, we just set the meter to active, to make sure that it is indeed active
     */
    after(int newWattage,PowerMeter pm): MeterMaxWattageIncreased(newWattage,pm){
        pm.setActive();
    }

}
