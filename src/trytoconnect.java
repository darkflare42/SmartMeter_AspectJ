/**
 * Created by Omer on 18/05/2016.
 */

import Engine.Address;
import Engine.Customer;
import Engine.PowerMeter;

import java.sql.*;
import java.util.Date;
import Engine.*;


public class trytoconnect {


    // Notice, do not import com.mysql.jdbc.*
// or you will have problems!
    public static void main(String[] args) throws  Exception{
        Date d=new Date();
        Address ddd= new Address("IL","no","maale","shaham",12);
        Customer cs= new Customer("omer","king",35,ddd);
       // DBComm.addNewCustomer(cs);
        PowerMeter pm =new PowerMeter(55,true,d,d,10,15,20,cs);
        PowerMeter g=DBComm.getMeterById(55);
        System.out.println("sfdsdf");
        System.out.print(g.getCostumerID());
    }

    public static void insertDataLogIn(int userID, String userName, String password) {

        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            String query = " insert into login (user_id, user_name, password)"
                    + " values (?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, userID);
            preparedStmt.setString(2, userName);
            preparedStmt.setString(3, password);
            // execute the preparedstatement
            preparedStmt.execute();
            // Do something with the Connection
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public static ResultSet getDataFromLoginByUserId(int userId) {
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            insertDataLogIn(324234, "Dgdg", "dfg");
            Statement st = conn.createStatement();
            ResultSet d = st.executeQuery("SELECT * FROM  login WHERE user_id=" + userId);
            // Do something with the Connection
            return d;
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendoreError: " + ex.getErrorCode());
        }

    return null;
    }



    public static void insertDataBill(int userId,int currentBill, Date lastDateToPay, boolean payedBool) {
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            String query = " insert into bill (user_id, current_bill, last_date_to_pay, payed)"
                    + " values (?, ?, ?,?)";

            // create the mysql insert preparedstatement
            Object timestamp = new java.sql.Timestamp(lastDateToPay.getTime());

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, userId);
            preparedStmt.setInt(2, currentBill);
            preparedStmt.setObject(3, timestamp);
            preparedStmt.setBoolean(4, payedBool);
            // execute the preparedstatement
            preparedStmt.execute();
            // Do something with the Connection
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    public static ResultSet getDataFromBillByUserId(int userId) {
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            insertDataLogIn(324234, "Dgdg", "dfg");
            Statement st = conn.createStatement();
            ResultSet d = st.executeQuery("SELECT * FROM  bill WHERE user_id=" + userId);
            // Do something with the Connection
            return d;
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendoreError: " + ex.getErrorCode());
        }

        return null;
    }



}


