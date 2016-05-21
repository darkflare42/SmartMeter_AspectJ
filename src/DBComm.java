import java.sql.*;
import java.util.*;

/**
 * Created by Or Keren on 05/05/2016.
 */
public class DBComm {

    private static Connection con;


    public static void init(){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                    "user=root&password=root");
            Statement stmnt = con.createStatement();


            ResultSet rs = stmnt.executeQuery("SELECT * FROM logintest");

            while(rs.next())
                System.out.println(rs.getInt(1) + " " + rs.getString(2) +" " + rs.getString(3));
            con.close();
        }
        catch (Exception e){
            System.out.println("Exception in" + DBComm.class.getClass().getName() + ": " + e.getMessage());
        }
    }


    public static void addNewMeter(PowerMeter newMeter){

    }

    public static void addNewCustomer(Customer newCustomer){

    }

    public static void addMeterToCustomer(PowerMeter meter, Customer customer){

    }

    public static void removeMeterFromCustomer(PowerMeter meter, Customer customer){

    }

    public static void deleteCustomer(Customer customer){

    }

    public static void deltePowerMeter(PowerMeter meter){

    }

    public static void getAllMeters(){

    }

    public static void getAllCustomers(){

    }

    public static boolean login(String username, String password){
        //TODO: Maybe we can merge this and the getUser method together
        return true;
    }

    public static User getUser(String username){
        return null;
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



    public static void insertDataBill(int userId, int currentBill, java.util.Date lastDateToPay, boolean payedBool) {
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
