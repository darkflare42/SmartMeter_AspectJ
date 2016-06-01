package Aspects;


import Engine.Customer;
import Engine.MainTest;
import Engine.PowerMeter;
import Engine.User;

import java.sql.ResultSet;
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
    pointcut secureMeterChanges(): call(* Engine.DBComm.deletePowerMeter(PowerMeter)) ||
                                   call(* Engine.DBComm.addNewMeter(PowerMeter)) ||
                                   call(* Engine.DBComm.updatemeter(PowerMeter));

    void around(PowerMeter meter): secureMeterChanges() && args(meter){
        System.out.println("================================================================================");
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return;
        }

        proceed(meter);

    }


    /**
     * pass
     */
    pointcut secureMeterInfo(): call(* Engine.DBComm.getMeterById(int));
    PowerMeter around(int id): secureMeterInfo() && args(id){
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return null;
        }
        return proceed(id);

    }

    /**
     * pass
     */
    pointcut secureMeterByLocation(): call(* Engine.DBComm.getAllMetersByCity(String));
    LinkedList<PowerMeter> around(String city): secureMeterByLocation() && args(city){
        if (MainTest.currentUser.getUserType() != User.userTypes.CUSTOMER) {
            return null;
        }
        return proceed(city);

    }


    /**
     * pass
     */
    pointcut secureMeterByUserId(): call(* Engine.DBComm.getAllMeterdByUserId(int));
    LinkedList<PowerMeter> around(int id): secureMeterByUserId() && args(id){
        System.out.print("!!");
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return null;
        }
        return proceed(id);

    }
    /**
     * pass
     */
    pointcut secureMeters(): call(* Engine.DBComm.getAllMeters());
    LinkedList<PowerMeter> around(): secureMeters() {
        System.out.print("9449");
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return null;
        }
        return proceed();

    }


    /**
     * pass
     */
    pointcut secureCustomersList(): call(* Engine.DBComm.addNewCustomer(Customer)) || call(* Engine.DBComm.deleteCustomer(Customer));
    void around(Customer cust): secureCustomersList() && args(cust){
        System.out.print("99");
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            System.out.println("delete costumer proceed fail");
            return;
        }
        System.out.println("delete costumer proceed");
        proceed(cust);

    }


    /**
     * pass
     */
    pointcut secureCustomeryUserId(): call(* Engine.DBComm.getCustumerById(int));
    Customer around(int id): secureCustomeryUserId() && args(id){
        System.out.print("%%%@@");
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return null;
        }
        return proceed(id);

    }

    /**
     * pass
     */
    pointcut secureCustomer(): call(* Engine.DBComm.getAllCustomers());
    LinkedList<Customer> around(): secureCustomer() {

        if (MainTest.currentUser.getUserType() == User.userTypes.ADMINISTRATOR) {
            return proceed();
        }
        return null;

    }


    /**
     * pass
     */
    pointcut secureBill(): call(* Engine.DBComm.insertDataBill(int,int,java.util.Date,boolean ));
    void around(int userId, int currentBill, java.util.Date lastDateToPay, boolean payedBool): secureBill() && args(userId,currentBill,lastDateToPay,payedBool){
        System.out.println("dfgdfgdfgdfg3");
        if (MainTest.currentUser.getUserType() == User.userTypes.ADMINISTRATOR) {
            proceed(userId,currentBill,lastDateToPay,payedBool);
        }
        return;

    }

    /**
     * secure bill tariff,PASS DEBUGGING
     */

    pointcut secureUserBill(): call(* Engine.DBComm.getDataFromBillByUserId(int));
    ResultSet around(int userID): secureUserBill() && args(userID){
        System.out.println("$$$");
        if (MainTest.currentUser.getUserType() == User.userTypes.ADMINISTRATOR || MainTest.currentUser.getID()==userID) {
            return proceed(userID);
        }
        return null;

    }


    /**
     * pass debugging
     */
    pointcut secureTaarif(): call(* Engine.DBComm.insertTarrifCity(String,int, int)) || call(* Engine.DBComm.insertTarrifDistrict(String,int, int)) || call(* Engine.DBComm.insertTarrifCountry(String,int, int));
    void around(String name,int id, int newTariff): secureTaarif() && args(name,id,newTariff){
        System.out.println("Sdf");
        if (MainTest.currentUser.getUserType() == User.userTypes.ADMINISTRATOR ) {
            proceed(name,id,newTariff);
        }
        return;

    }










}
