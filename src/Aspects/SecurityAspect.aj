package Aspects;


import Engine.MainTest;
import Engine.PowerMeter;
import Engine.User;

/**
 *  This aspect handles extra security on specific methods, for example. Deleting a meter
 *  should only be allowed for specific types of users (example specific types of admins, or specific types of managers)
 *  Adds an extra level of security on top of just GUI
 *  Technically, all the pointcuts here should be "within" because they should "hijack" the method's code and decide
 *  whether to continue or not
 * Created by Or Keren on 21/05/2016.
 */
public aspect SecurityAspect {
    //TODO: See if we can add more security elements in here

    pointcut delt() : call(* Engine.DBComm.deltePowerMeter);
    int around(PowerMeter meter): delt{
        System.out.println("hello to you");
        if (MainTest.currentUser.getUserType()== User.userTypes.CUSTOMER){
            return -1;
        }
        proceed(meter);

    }



    pointcut MeterScreenOpened();

}
