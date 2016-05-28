package Aspects;


import Engine.Customer;
import Engine.MainTest;
import Engine.PowerMeter;
import Engine.User;

import java.util.LinkedList;

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

   // pointcut delt() : call(* Engine.DBComm.deltePowerMeter);
    pointcut secureMeterChanges(): call(* Engine.DBComm.deltePowerMeter(PowerMeter)) || call(* Engine.DBComm.addNewMeter(PowerMeter)) || call(* Engine.DBComm.updatemeter(PowerMeter));
    void around(PowerMeter meter): secureMeterChanges() && args(meter){
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return;
        }
        proceed(meter);

    }





    pointcut secureMeterInfo(): call(* Engine.DBComm.getMeterById(int));
    PowerMeter around(int id): secureMeterInfo() && args(id){
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return null;
        }
        return proceed(id);

    }


    pointcut secureMeterByLocation(): call(* Engine.DBComm.getAllMetersByCity(String));
    LinkedList<PowerMeter> around(String city): secureMeterByLocation() && args(city){
        if (MainTest.currentUser.getUserType() != User.userTypes.CUSTOMER) {
            return null;
        }
        return proceed(city);

    }



    pointcut secureMeterByUserId(): call(* Engine.DBComm.getAllMeterdByUserId(int));
    LinkedList<PowerMeter> around(int id): secureMeterByUserId() && args(id){
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return null;
        }
        return proceed(id);

    }

    pointcut secureMeters(): call(* Engine.DBComm.getAllMeters());
    LinkedList<PowerMeter> around(): secureMeters() {
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return null;
        }
        return proceed();

    }



    pointcut secureCustomersList(): call(* Engine.DBComm.addNewCustomer()) || call(* Engine.DBComm.deleteCustomer());
    void around(Customer cust): secureCustomersList() && args(cust){
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return;
        }
        proceed(cust);

    }





    pointcut secureCustomeryUserId(): call(* Engine.DBComm.getCustumerById(int));
    Customer around(int id): secureCustomeryUserId() && args(id){
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return null;
        }
        return proceed(id);

    }


    pointcut secureCustomer(): call(* Engine.DBComm.getAllCustomers());
    LinkedList<Customer> around(): secureCustomer() {
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return null;
        }
        return proceed();

    }



    pointcut secureBill(): call(* Engine.DBComm.getAllCustomers());
    void around(int userId, int currentBill, java.util.Date lastDateToPay, boolean payedBool): secureBill() && args(userId,currentBill,lastDateToPay,payedBool){
        if (MainTest.currentUser.getUserType() == User.userTypes.ADMINISTRATOR) {
            proceed(userId,currentBill,lastDateToPay,payedBool);
        }
        return;

    }

    pointcut secureUserBill(): call(* Engine.DBComm.getDataFromBillByUserId(int));
    void around(int userID): secureUserBill() && args(userID){
        if (MainTest.currentUser.getUserType() == User.userTypes.ADMINISTRATOR || MainTest.currentUser.getID()==userID) {
            proceed(userID);
        }
        return;

    }



    pointcut secureTaarif(): call(* Engine.DBComm.insertTarrifCity(String,int, int)) || call(* Engine.DBComm.insertTarrifDistrict(String,int, int)) || call(* Engine.DBComm.insertTarrifCountry(String,int, int));
    void around(String name,int id, int newTariff): secureTaarif() && args(name,id,newTariff){
        if (MainTest.currentUser.getUserType() == User.userTypes.ADMINISTRATOR ) {
            proceed(name,id,newTariff);
        }
        return;

    }

















    //pointcut MeterScreenOpened();

}
