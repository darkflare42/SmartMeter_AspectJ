package Aspects;
import com.sun.xml.internal.ws.api.pipe.Engine;

import java.sql.*;
import Engine.*;
import Engine.DBComm;

/**
 * Created by Or Keren on 21/05/2016.
 */






public aspect SendReportAspect {

    pointcut CustomerDisconnected(Customer user):execution (* Engine.DBComm.deleteCustomer(Customer)) && args(user);

    before(Customer user): CustomerDisconnected(user) {
        System.out.println("CustomerDisconnected");
        ResultSet d = DBComm.getDataFromBillByUserId(user.getID());
        try {
            d.next();
            int bill = d.getInt(1);
            BillingEngine.sendBillingReportByMail(bill, user);
        }
        catch(Exception ex){
            return;
        }

    }

    pointcut FirstOfMonth():execution(* Engine.BillingEngine.checkMonthlyBilling());
    before(): FirstOfMonth() {
        ResultSet d = DBComm.getDataFromBillByUserId(MainTest.currentUser.getID());
        try {
            d.next();
            int bill = d.getInt(1);
            BillingEngine.sendBillingReportByMail(bill, MainTest.currentUser);
        }
        catch(Exception ex){
            return;
        }

    }
}
