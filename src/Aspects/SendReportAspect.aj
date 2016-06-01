//package Aspects;
//import com.sun.xml.internal.ws.api.pipe.Engine;
//
//import java.sql.*;
//import Engine.*;
//import Engine.DBComm;
//
///**
// * Created by Or Keren on 21/05/2016.
// */
//
//
//
//
//
//
//public aspect SendReportAspect {
//
//    pointcut CustomerDisconnected(int userId):execution (* Engine.DBComm.deleteCustomer(int)) && args(usedId);
//
//    before(int userId): CustomerDisconnected(UsedId) throws exception{
//        ResultSet d = DBComm.getDataFromBillByUserId(userId);
//        try {
//            d.next();
//            return d.getInt(1);
//            Customer ct= DBComm.getCustumerById(userId);
//
//        }
//        catch(Exception ex){
//            return;
//        }
//
//    }
//
//
//
//
//
//
//
//
//
//
//    pointcut UserRequestedReport(); //TODO: Not really sure this is needed
//
//    pointcut FirstOfMonth(); //TODO: Not sure this is needed
//}
