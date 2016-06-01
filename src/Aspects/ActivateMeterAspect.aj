package Aspects;
import Engine.*;

import java.util.LinkedList;

/**
 * This aspect deals with activating a meter (setting the status to ACTIVE)
 * Created by Or Keren on 25/05/2016.
 */
public aspect ActivateMeterAspect {

    pointcut ClientLateBillPayed(Customer c, Bill b) : execution(* Engine.BillingEngine.PayBill(Customer, Bill))
                                                        && args(c, b);


    after(Customer c, Bill b):ClientLateBillPayed(c,b){
        LinkedList<PowerMeter> allMeters = DBComm.getAllMeterdByUserId(c.getID());
        for(int i=0;i<allMeters.size();i++){
            allMeters.get(i).setActive();
        }
    }

    pointcut CityOverloadFixed(String city) : execution(* Communicator.overloadFixed(String)) && args(city);

    after(String city):CityOverloadFixed(city){
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
     * This pointcut is for reactivating meters that have max wattage set - we need to reset the wattage reading
     * and also activate meters that have been inactivated because they reached max wattage
     */
    pointcut ReactivatePrepaid() : execution(* BillingEngine.FirstOfMonth());
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

    pointcut MeterMaxWattageIncreased(int newWattage,PowerMeter pm) : execution(* PowerMeter.setMaxWattage(int)) && args(newWattage) && this(pm);


    after(int newWattage,PowerMeter pm): MeterMaxWattageIncreased(newWattage,pm){
        pm.setActive();
    }









}
