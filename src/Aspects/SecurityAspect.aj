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
 *
 *  For testing purposes we only check the type of user of the system. However, the aspect can be much more "clever"
 *  by actually retrieving other information regarding the current user and regulating the information according
 *  to his jurisdiction for example
 */
public aspect SecurityAspect {

    /**
     * This pointcut is for when sensitive information regarding the meter occurs - adding, updating or deleting
     */
    pointcut secureMeterChanges(): call(* Engine.DBComm.deletePowerMeter(PowerMeter)) ||
                                   call(* Engine.DBComm.addNewMeter(PowerMeter)) ||
                                   call(* Engine.DBComm.updatemeter(PowerMeter));

    /**
     * The advice regulates the function by making sure that the user type is indeed authorised to enter
     */
    void around(PowerMeter meter): secureMeterChanges() && args(meter){
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return;
        }

        proceed(meter);

    }


    /**
     * This pointcut occurs when someone wants to retrieve information about a meter from the DB
     */
    pointcut secureMeterInfo(): call(* Engine.DBComm.getMeterById(int));

    /**
     * The advice regulates the function by making sure that the user type is indeed authorised to enter
     */
    PowerMeter around(int id): secureMeterInfo() && args(id){
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return null;
        }
        return proceed(id);

    }

    /**
     * This pointcut is also concerned with the retrieval of information regarding a meter from the DB
     */
    pointcut secureMeterByLocation(): call(* Engine.DBComm.getAllMetersByCity(String));

    /**
     * The advice regulates the function by making sure that the user type is indeed authorised to enter
     */
    LinkedList<PowerMeter> around(String city): secureMeterByLocation() && args(city){
        if (MainTest.currentUser.getUserType() != User.userTypes.CUSTOMER) {
            return null;
        }
        return proceed(city);

    }


    /**
     * This pointcut is again, concerned with the retrieval of information regarding meters
     */
    pointcut secureMeterByUserId(): call(* Engine.DBComm.getAllMeterdByUserId(int));

    /**
     * The advice regulates the function by making sure that the user type is indeed authorised to enter
     */
    LinkedList<PowerMeter> around(int id): secureMeterByUserId() && args(id){
        System.out.print("!!");
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return null;
        }
        return proceed(id);

    }

    /**
     * The advice regulates the function by making sure that the user type is indeed authorised to enter
     */
    pointcut secureMeters(): call(* Engine.DBComm.getAllMeters());

    /**
     * The advice regulates the function by making sure that the user type is indeed authorised to enter
     */
    LinkedList<PowerMeter> around(): secureMeters() {
        System.out.print("9449");
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return null;
        }
        return proceed();

    }


    /**
     * This pointcut occurs whenever someone touches the user-base of the system
     */
    pointcut secureCustomersList(): call(* Engine.DBComm.addNewCustomer(Customer)) ||
                                        call(* Engine.DBComm.deleteCustomer(Customer));

    /**
     * The advice regulates the function by making sure that the user type is indeed authorised to enter
     */
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
     * This pointcut occurs when someone wants to retrieve information about a certain customer from the DB
     */
    pointcut secureCustomeryUserId(): call(* Engine.DBComm.getCustumerById(int));

    /**
     * The advice regulates the function by making sure that the user type is indeed authorised to enter
     */
    Customer around(int id): secureCustomeryUserId() && args(id){
        System.out.print("%%%@@");
        if (MainTest.currentUser.getUserType() == User.userTypes.CUSTOMER) {
            return null;
        }
        return proceed(id);

    }

    /**
     * This pointcut occurs when someone wants to retrieve information about all customers from the DB
     */
    pointcut secureCustomer(): call(* Engine.DBComm.getAllCustomers());

    /**
     * The advice regulates the function by making sure that the user type is indeed authorised to enter
     */
    LinkedList<Customer> around(): secureCustomer() {

        if (MainTest.currentUser.getUserType() == User.userTypes.ADMINISTRATOR) {
            return proceed();
        }
        return null;

    }


    /**
     * This pointcut occurs when someone enters a new bill for a customer
     */
    pointcut secureBill(): call(* Engine.DBComm.insertDataBill(int,int,java.util.Date,boolean ));

    /**
     * The advice regulates the function by making sure that the user type is indeed authorised to enter
     */
    void around(int userId, int currentBill, java.util.Date lastDateToPay, boolean payedBool): secureBill() && args(userId,currentBill,lastDateToPay,payedBool){
        System.out.println("dfgdfgdfgdfg3");
        if (MainTest.currentUser.getUserType() == User.userTypes.ADMINISTRATOR) {
            proceed(userId,currentBill,lastDateToPay,payedBool);
        }
        return;

    }

    /**
     * * This pointcut occurs when someone retrieves a bill from a certain user
     */
    pointcut secureUserBill(): call(* Engine.DBComm.getDataFromBillByUserId(int));

    /**
     * The advice regulates the function by making sure that the user type is indeed authorised to enter
     */
    ResultSet around(int userID): secureUserBill() && args(userID){
        System.out.println("$$$");
        if (MainTest.currentUser.getUserType() == User.userTypes.ADMINISTRATOR || MainTest.currentUser.getID()==userID) {
            return proceed(userID);
        }
        return null;

    }


    /**
     * * This pointcut occurs when someone changes a tariff for a city district or country
     */
    pointcut secureTaarif(): call(* Engine.DBComm.insertTarrifCity(String,int, int)) ||
                                call(* Engine.DBComm.insertTarrifDistrict(String,int, int)) ||
                                call(* Engine.DBComm.insertTarrifCountry(String,int, int));

    /**
     * The advice regulates the function by making sure that the user type is indeed authorised to enter
     */
    void around(String name,int id, int newTariff): secureTaarif() && args(name,id,newTariff){
        System.out.println("Sdf");
        if (MainTest.currentUser.getUserType() == User.userTypes.ADMINISTRATOR ) {
            proceed(name,id,newTariff);
        }
        return;

    }










}
