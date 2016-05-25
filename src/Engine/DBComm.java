package Engine;

import java.sql.*;
import java.util.*;
import java.util.Date;

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



    /**
     * meter columns:
     * user_id
     * meter_id
     * active
     * init_date
     * last_read
     * max_wattage
     * total_wattage
     * current_wattage
     * @param newMeter
     */
    public static void addNewMeter(PowerMeter newMeter){

        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            String query = " insert into meter (user_id, meter_id, active, init_date, last_read, max_wattage, total_wattage, current_wattage)"
                    + " values (?, ?, ?, ? , ?, ?, ?, ?)";

            Object timestamp = new java.sql.Timestamp(newMeter.getStartOperationDate().getTime());


            Object lastReadStamp = new java.sql.Timestamp(newMeter.getLastReadDate().getTime());

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, newMeter.getCostumerID());
            preparedStmt.setInt(2, newMeter.getID());
            preparedStmt.setBoolean(3, newMeter.getIsActive());
            preparedStmt.setObject(4, timestamp);
            preparedStmt.setObject(5, lastReadStamp);

            preparedStmt.setInt(6, newMeter.getMaxWattage());
            preparedStmt.setInt(7, newMeter.getTotalReading());
            preparedStmt.setInt(8, newMeter.readWattage());

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









    public static void deltePowerMeter(PowerMeter meter) {
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            String query = " DELETE FROM meter WHERE meter_id=" + meter.getID();

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            preparedStmt.execute();
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException11: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }


    }








    public static PowerMeter getMeterById(int meterId){
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            String query = "SELECT * FROM meter WHERE meter_id="+meterId;

            Statement st = conn.createStatement();
            ResultSet d = st.executeQuery(query );
            // Do something with the Connection

            d.next();
            int userId= d.getInt("user_id");

            boolean active = d.getBoolean("active");


            Date init_Date = d.getDate(4);
            System.out.println("Dgdfdgdgfg");
            Date lastRead = d.getDate(5);
            int maxWattate = d.getInt(6);
            int totalWattage = d.getInt(7);
            int currentWattage = d.getInt(8);
            System.out.println(currentWattage);
            System.out.println("userid"+userId);
            Customer cus =getCustumerById(userId);
            System.out.println("$$$");
            PowerMeter pm = new PowerMeter(meterId,active,init_Date,lastRead,maxWattate,totalWattage,currentWattage,cus);
            System.out.println("$$$ff");
            return pm;
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException55: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }


        return null;
    }


    public static LinkedList<PowerMeter> getAllMetersByCity(String city){
        LinkedList<PowerMeter> allMeters =getAllMeters();
        for(int i=0; i<allMeters.size(); i++){
            if(!allMeters.get(i).getCurrentLocation().getCity().equals(city)){
                allMeters.remove(i);
            }
        }
        return allMeters;


    }


    public static LinkedList<PowerMeter> getAllMeterdByUserId(int userId){
        LinkedList<PowerMeter> allMeters =getAllMeters();
        for(int i=0; i<allMeters.size(); i++){
            if(allMeters.get(i).getCostumerID()!= userId){
                allMeters.remove(i);
            }
        }
        return allMeters;


    }



    public static LinkedList<PowerMeter> getAllMeters(){
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");

            Statement st = conn.createStatement();
            ResultSet d = st.executeQuery("SELECT meter_id FROM  meter" );
            // Do something with the Connection
            LinkedList<PowerMeter> meters = new LinkedList<PowerMeter>();
            while (d.next()){
                PowerMeter meter = getMeterById(d.getInt(1));
                meters.add(meter);
            }
            return meters;
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }




        return null;


    }



    public static void updatemeter(PowerMeter meter){



        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");

            String query = "UPDATE meter  SET active="+meter.getIsActive()+" , last_read="+
                    meter.getLastReadDate()+" , max_wattage="+meter.getMaxWattage()+
                    " ,total_wattage="+meter.getTotalReading()+" , current_wattage="+meter.readWattage();

            Statement st = conn.createStatement();
            st.executeQuery(query);
            // Do something with the Connection

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }


    }










    /**
     * user table:
     * used_id
     * first_name
     * last_name
     * country
     * district
     * city
     * house_number
     * user_type
     * @param newCustomer
     */
    public static void addNewCustomer(Customer newCustomer){

        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            String query = " insert into user (user_id, first_name, last_name, country, district, city, house_number, user_type, street)"
                    + " values (?, ?, ?, ? , ?, ?, ?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, newCustomer.getID());
            preparedStmt.setString(2, newCustomer.getFirstName());
            preparedStmt.setString(3, newCustomer.getSurname());
            preparedStmt.setString(4, newCustomer.getAddress().getCountry());
            preparedStmt.setString(5, newCustomer.getAddress().getDistrict());
            preparedStmt.setString(6, newCustomer.getAddress().getCity());
            preparedStmt.setInt(7,newCustomer.getAddress().getHouseNumber());
            preparedStmt.setInt(8, 1); //TODO determinate how user_type wil represent.
            preparedStmt.setString(9,newCustomer.getAddress().getStreetName());

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





    public static void deleteCustomer(Customer customer){
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            String query = " DELETE FROM user WHERE user_id="+customer.getID();

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            preparedStmt.execute();
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }




    }



    //todo getAllActieMeters, getallInActiveMeters

    public static Customer getCustumerById(int userId){
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");

            String query = "select * FROM user WHERE user_id= "+userId;
            System.out.println(query);

            Statement st = conn.createStatement();
            ResultSet t=st.executeQuery(query);
            t.next();
            Address address =new Address(t.getString(4),t.getString(5), t.getString(9), t.getInt(7));
            Customer customer = new Customer(t.getString(2),t.getString(3), userId, address);
            return customer;

            // Do something with the Connection

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLExceptionbr: " + ex.getMessage());
            System.out.println("SQLExceptionbr: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }






        return null;
    }




    public static LinkedList<Customer> getAllCustomers(){

        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");

            String query = "select * FROM user ";

            Statement st = conn.createStatement();
            ResultSet t=st.executeQuery(query);
            LinkedList<Customer>  allCust= new LinkedList<Customer>();
            while(t.next()) {
                System.out.println(t.getInt(1));
                Customer customer = getCustumerById(t.getInt(1));
                allCust.add(customer);
            }

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }






        return null;
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
