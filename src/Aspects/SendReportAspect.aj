package Aspects;
import com.sun.xml.internal.ws.api.pipe.Engine;

import java.sql.*;
import Engine.*;
import Engine.DBComm;

/**
 * Created by Or Keren on 21/05/2016.
 */






public aspect SendReportAspect {

    pointcut CustomerDisconnected(Customer user):execution (* Engine.DBComm.deleteCustomer(Custumer)) && args(user);

    before(Customer user): CustomerDisconnected(user) {
        System.out.println("omer");
        ResultSet d = DBComm.getDataFromBillByUserId(user.getID());
        try {
            d.next();
            int bill = d.getInt(1);
            BillingEngine.sendBillingReportByMail(bill, user);
            System.out.println("sdfr");
        }
        catch(Exception ex){
            return;
        }

    }










    pointcut UserRequestedReport(); //TODO: Not really sure this is needed

    pointcut FirstOfMonth(); //TODO: Not sure this is needed
}
