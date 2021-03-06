package Engine;
import java.sql.Date;
import java.sql.*;
import java.util.*;
//import java.util.Date;


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
            preparedStmt.setInt(1, newMeter.getCustomerID());
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

    public static void deletePowerMeter(PowerMeter meter) {
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
            System.out.println("SQLException: " + ex.getMessage());
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

            Date lastRead = d.getDate(5);
            int maxWattate = d.getInt(6);
            int totalWattage = d.getInt(7);
            int currentWattage = d.getInt(8);
            System.out.println(currentWattage);
            System.out.println("userid"+userId);
            Customer cus =getCustumerById(userId);

            PowerMeter pm = new PowerMeter(meterId,active,init_Date,lastRead,maxWattate,totalWattage,currentWattage,cus);

            return pm;
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
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
            if(allMeters.get(i).getCustomerID()!= userId){
                allMeters.remove(i);
            }
        }
        System.out.println(allMeters);
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

    /**
     * pass debugging
     * @param meter
     */
    public static void updateMeter(PowerMeter meter){
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");

            String query = "UPDATE meter  SET user_id=? , active=?, init_date=?, last_read=?, max_wattage=?, total_wattage=?, current_wattage = ? Where meter_id=?";

            Object lastReadStamp = new java.sql.Timestamp(meter.getLastReadDate().getTime());
            Object initStamp = new java.sql.Timestamp(meter.getStartOperationDate().getTime());


            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1,meter.getCustomerID());
            ps.setBoolean(2,meter.getIsActive());
            ps.setObject(3,initStamp);
            ps.setObject(4,lastReadStamp);
            ps.setInt(5, meter.getMaxWattage());
            ps.setInt(6, meter.getTotalReading());
            ps.setInt(7, meter.readWattage());
            ps.setInt(8,meter.getID());

            ps.executeUpdate();




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
        System.out.println("delete Customer");
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

    public static LinkedList<PowerMeter> getAllActiveMeter(){
        LinkedList<PowerMeter> allMeters= getAllMeters();
        LinkedList<PowerMeter> allActive =new LinkedList<PowerMeter>();
        for(int i=0;i<allMeters.size();i++){
            if( allMeters.get(i).getIsActive()){
                allActive.push(allMeters.get(i));
            }
        }
        return allActive;

    }

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
            System.out.println("SQLException: " + ex.getMessage());
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
            return allCust;

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }






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

    /**
     * pass debugging
     * @param city
     * @return
     */
    public static int getCityTaarif(String city){
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            Statement st = conn.createStatement();
            PreparedStatement tr = conn.prepareStatement("SELECT * FROM  tariff_city WHERE city_name=?");
            tr.setString(1,city);
            ResultSet d = tr.executeQuery();
            System.out.println("finish get city tariff");
            d.next();
            return d.getInt("tariff");
            // Do something with the Connection
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendoreError: " + ex.getErrorCode());
        }

        return -1;

    }

    /**
     * pass debugging
     * @param country
     * @return
     */
    public static int getCountryTaarif(String country){
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            PreparedStatement tr = conn.prepareStatement("SELECT * FROM  tariff_country WHERE country_name=?");
            tr.setString(1,country);

            ResultSet d = tr.executeQuery();
            d.next();
            return d.getInt("tariff");
            // Do something with the Connection
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendoreError: " + ex.getErrorCode());
        }

        return -1;

    }

    /**
     * pass debugging
     * @param district
     * @return
     */
    public static int getDistrictTaarif(String district){
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            PreparedStatement tr = conn.prepareStatement("SELECT * FROM  district WHERE district=?");
            tr.setString(1,district);

            ResultSet d = tr.executeQuery();
            d.next();
            return d.getInt("tariff");
            // Do something with the Connection
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendoreError: " + ex.getErrorCode());
        }

        return -1;

    }

    /**
     * pass dubugging.
     * @param city
     * @param cityID
     * @param taarif
     */
    public static void insertTarrifCity(String city, int cityID, int taarif) {

        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            String query = " insert into tariff_city (city_name, city_id, tariff)"
                    + " values (?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, city);
            preparedStmt.setInt(2, cityID);
            preparedStmt.setInt(3, taarif);
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

    /**
     * pass debugging.
     * @param country
     * @param coountryID
     * @param taarif
     */
    public static void insertTarrifCountry(String country, int coountryID, int taarif) {

        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            String query = " insert into tariff_country (country_name, country_id, tariff)"
                    + " values (?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, country);
            preparedStmt.setInt(2, coountryID);
            preparedStmt.setInt(3, taarif);
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

    /**
     * pass debugging
     * @param district
     * @param districtID
     * @param taarif
     */
    public static void insertTarrifDistrict(String district, int districtID, int taarif) {

        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            String query = " insert into tariff_district (district_name, district_id, tariff)"
                    + " values (?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, district);
            preparedStmt.setInt(2, districtID);
            preparedStmt.setInt(3, taarif);
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

    /**
     * pass debugging
     * @param city
     * @param newTariff
     */
    public static void updateCityTariff(String city,int newTariff){



        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");

            String query = "UPDATE tariff_city  SET tariff = ? WHERE city_name = ?";

            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println(query);

            ps.setInt(1,newTariff);
            ps.setString(2,city);

            ps.executeUpdate();
            // Do something with the Connection

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }


    }

    /**
     * pass debugging
     * @param district
     * @param newTariff
     */
    public static void updateDistrictTariff(String district,int newTariff) {

        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            String query = "UPDATE tariff_district  SET tariff = ? WHERE district_name = ?";

            PreparedStatement ps = conn.prepareStatement(query);


            ps.setInt(1,newTariff);
            ps.setString(2,district);

            ps.executeUpdate();

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    /**
     * pass debugging
     * @param country
     * @param newTariff
     */
    public static void updateCountryTariff(String country,int newTariff){



        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            String query = "UPDATE tariff_country  SET tariff = ? WHERE country_name = ?";

            PreparedStatement ps = conn.prepareStatement(query);


            ps.setInt(1,newTariff);
            ps.setString(2,country);

            ps.executeUpdate();

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }


    }

    public static void insertBill(Bill b){
        insertDataBill(b.getCustomer().getID(), (int)b.getPrice(), b.getCutoffDate(), false);
    }

    public static Bill getBillByCustomerID(int customerID){
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");

            String query = "select * FROM bill WHERE user_id= "+customerID;


            //String query = " insert into bill (user_id, current_bill, last_date_to_pay, payed)"
            //        + " values (?, ?, ?,?)";


            Statement st = conn.createStatement();
            ResultSet d = st.executeQuery(query );
            // Do something with the Connection

            d.next();
            Customer c = getCustumerById(customerID);

            int currentPrice = d.getInt(2);
            Date cutOffDate = d.getDate(3);
            boolean payed = d.getBoolean(4);

            return new Bill(c, currentPrice, cutOffDate, null, payed);

            // Do something with the Connection

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return null;

    }

    public static void updateBill(Bill b){
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");

            String query = "UPDATE bill  SET current_bill=?, last_date_to_pay=?, payed=? Where user_id=?";

            //String query = " insert into bill (user_id, current_bill, last_date_to_pay, payed)"
            //        + " values (?, ?, ?,?)";

            Object timestamp = new java.sql.Timestamp(b.getCutoffDate().getTime());


            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1,(int)b.getPrice());
            ps.setObject(2, timestamp);
            ps.setBoolean(3, b.getPayed());
            ps.setInt(4, b.getCustomer().getID());

            ps.executeUpdate();

            // Do something with the Connection

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

    }

    public static LinkedList<Bill> getAllBills(){
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");

            LinkedList<Bill> billList = new LinkedList<>();
            Statement st = conn.createStatement();
            ResultSet d = st.executeQuery("SELECT * FROM bill");
            while(d.next()){
                int userID = d.getInt(1);
                int currentPrice = d.getInt(2);
                Date cutOffDate = d.getDate(3);
                boolean payed = d.getBoolean(4);
                billList.add(new Bill(getCustumerById(userID), currentPrice, cutOffDate, null, payed));
            }
            return billList;

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return null;
    }

    public static LinkedList<Customer> getCustomersFromCountry(String country){
        LinkedList<Customer> customers = getAllCustomers();
        LinkedList<Customer> filteredCustomers = new LinkedList<>();
        for(Customer c : customers){
            if(c.getAddress().getCountry().equals(country))
                    filteredCustomers.add(c);
        }
        return filteredCustomers;
    }

    public static LinkedList<Customer> getCustomersFromCity(String country, String city){
        LinkedList<Customer> customers = getAllCustomers();
        LinkedList<Customer> filteredCustomers = new LinkedList<>();
        for(Customer c : customers){
            if(c.getAddress().getCountry().equals(country) && c.getAddress().getCity().equals(city))
                filteredCustomers.add(c);
        }
        return filteredCustomers;
    }

    public static LinkedList<String> getAllCountries(){

        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");

            LinkedList<String> countries = new LinkedList<>();
            Statement st = conn.createStatement();
            ResultSet d = st.executeQuery("SELECT * FROM tariff_country");
            while(d.next()){
                countries.add(d.getString("country_name"));

            }
            return countries;

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return null;
    }

    public static LinkedList<String> getAllCities(){

        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");

            LinkedList<String> cities = new LinkedList<>();
            Statement st = conn.createStatement();
            ResultSet d = st.executeQuery("SELECT * FROM tariff_city");
            while(d.next()){
                cities.add(d.getString("city_name"));
            }
            return cities;

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return null;
    }
}
