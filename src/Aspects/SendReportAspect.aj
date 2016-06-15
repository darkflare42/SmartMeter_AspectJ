package Aspects;

import java.sql.*;
import java.util.LinkedList;

import Engine.*;
import Engine.DBComm;

/**
 * This aspect is concerned with sending reports to the user, there are a couple of places where we want to deal with
 * and send a bill/report to the user
 */
public aspect SendReportAspect {

    /**
     * This pointcut occurs when a customer has disconnected from the company. In another aspect we delete his
     * meters, and here we look at all the meters that he has and print a final bill for him. Due to this
     * this aspect must run BEFORE the DisconnectMeterAspect
     */
    pointcut CustomerDisconnected(Customer user):execution (* Engine.DBComm.deleteCustomer(Customer)) && args(user);

    /**
     * This pointcut occurs on a monthly basis. We go through all the customers, retrieve the bill and send it to them
     */
    pointcut FirstOfMonth():execution(* Engine.BillingEngine.checkMonthlyBilling());

    /**
     * This advice runs before the execution of the function, takes the current bill for the customer and sends
     * it to him
     */
    before(Customer user): CustomerDisconnected(user) {
        sendReportsForCustomer(user.getID());
    }

    /**
     * The advice occurs after the function is called by the BillingEngine, since BillingEngine calculates the bill
     * and this advice will then send the updated, calculated bill to the customer
     */
    after(): FirstOfMonth() {
        LinkedList<Customer> allCustomers = DBComm.getAllCustomers();
        for(Customer c : allCustomers){
            sendReportsForCustomer(c.getID());
        }
    }

    /**
     * Helper fuction, goes over all of a customer's bills, and sends them to the customer via mail & e-mail
     * @param customerID The customer ID to send bills to
     */
    private void sendReportsForCustomer(int customerID){
        ResultSet d = DBComm.getDataFromBillByUserId(customerID);
        try {
            d.next();
            int bill = d.getInt(1);
            BillingEngine.sendBillingReportByMail(bill, MainTest.currentUser);
        }
        catch(NullPointerException| SQLException ex2){
            return;
        }
    }
}
