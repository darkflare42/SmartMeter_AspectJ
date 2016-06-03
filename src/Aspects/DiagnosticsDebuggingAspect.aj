package Aspects;

import Engine.*;

import java.sql.*;
import java.util.*;
import java.util.Date;

/** This aspect is used to allow using the system in a sandbox.
 * Some major operations that the user can do will be different when in this diagnostics/debugging mode. This aspect
 * defines the behaviour of these functions
 */
public aspect DiagnosticsDebuggingAspect {

    private static Connection con;
    private static boolean initExecuted = false;
    private static int DiagnosticsCustomerID = -9999;


    /**
     * This pointcut happens when a new meter is added, for diagnostic purposes we want to write into a
     * different DB connection rather than the one in the system, this pointcut happens only when the system
     * is in diagnosticsMode
     */
    pointcut NewMeterAdded(PowerMeter newMeter): execution(* DBComm.addNewMeter(PowerMeter)) &&
                                            args(newMeter) && if(MainTest.diagnosticsMode);

    /**
     * This pointcut occurs when a meter is removed from the system, again it only occurs when the system is in
     * diagnostics mode
     */
    pointcut MeterRemoved(PowerMeter m): execution(* DBComm.deletePowerMeter(PowerMeter)) &&
                                            args(m) && if(MainTest.diagnosticsMode);

    /**
     * This pointcut occurs when a meter is updated in the system, again it only occurs when the system is in
     * diagnostics mode
     */
    pointcut MeterUpdated(PowerMeter m) : execution(* DBComm.updateMeter(PowerMeter)) &&
                                            args(m) && if(MainTest.diagnosticsMode);

    /**
     * This pointcut occurs when on a monthly basis - when checkMonthlyBilling is called. We use this to update
     * the current billing according to diagnostic rules, rather than real life rules
     */
    pointcut UpdateCustomerBilling() : execution(* BillingEngine.checkMonthlyBilling()) &&
                                            if(MainTest.diagnosticsMode);


    /**
     * This advice "takes over" the add new meter function and does its own version of add
     * first it adds into the TestSchema and then manipulates certain members in the meter
     * needed for diagnostics
     */
    void around(PowerMeter newMeter) : NewMeterAdded(newMeter){
        System.out.println("IN DIAGNOSTICS NEW METER ADDED");
        init();

        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgridDiagnostics?" +
                            "user=root&password=root");
            String query = " insert into meter (user_id, meter_id, active, init_date, last_read, " +
                    "max_wattage, total_wattage, current_wattage)"
                    + " values (?, ?, ?, ? , ?, ?, ?, ?)";

            Object timestamp = new java.sql.Timestamp(newMeter.getLastReadDate().getTime());

            //First change is that the start operationdate is ALWAYS the day it is added to the DB, and
            // not necessarily the actual date that it was set up on site
            Calendar cal = Calendar.getInstance();
            Date now = cal.getTime();
            Object nowTimeStamp = new java.sql.Timestamp(now.getTime());



            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            //Second change is that the customer ID is -9999 or some other constant value defined above
            preparedStmt.setInt(1, DiagnosticsCustomerID);
            preparedStmt.setInt(2, newMeter.getID());
            preparedStmt.setBoolean(3, newMeter.getIsActive());
            preparedStmt.setObject(4, nowTimeStamp);
            preparedStmt.setObject(5, timestamp); //The last readTime is

            //Third changes are for meter readings, they are all 9999
            preparedStmt.setInt(6, -1); //Infinite
            preparedStmt.setInt(7, 9999);
            preparedStmt.setInt(8, 9999);

            // execute the preparedstatement
            preparedStmt.execute();
            // Do something with the Connection
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return;
    }

    /**
     * This advice "takes over" the remove meter function and does its own version of delete
     * In this scenario we add the deleted meter to a special DB Table which records the deleted meters during
     * debugging (for restoring purposes)
     */
    void around(PowerMeter m) : MeterRemoved(m){
        System.out.println("IN DIAGNOSTICS METER REMOVED");
        init();
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgridDiagnostics?" +
                            "user=root&password=root");
            String query = " DELETE FROM meter WHERE meter_id=" + m.getID();

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            preparedStmt.execute();


            //The change we do here, is add a record of the meter deletion into a special table, that is used for
            //diagnostics and restoration purposes. This time all the information is saved as is

            String insertQuery = " insert into metersDeletedDiag (user_id, meter_id, active, init_date, last_read, " +
                    "max_wattage, total_wattage, current_wattage)"
                    + " values (?, ?, ?, ? , ?, ?, ?, ?)";

            Object timestamp = new java.sql.Timestamp(m.getStartOperationDate().getTime());


            Object lastReadStamp = new java.sql.Timestamp(m.getLastReadDate().getTime());

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmtAdd = conn.prepareStatement(insertQuery);
            preparedStmtAdd.setInt(1, m.getCustomerID());
            preparedStmtAdd.setInt(2, m.getID());
            preparedStmtAdd.setBoolean(3, m.getIsActive());
            preparedStmtAdd.setObject(4, timestamp);
            preparedStmtAdd.setObject(5, lastReadStamp);

            preparedStmtAdd.setInt(6, m.getMaxWattage());
            preparedStmtAdd.setInt(7, m.getTotalReading());
            preparedStmtAdd.setInt(8, m.readWattage());

            // execute the preparedstatement
            preparedStmtAdd.execute();



        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException11: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return;
    }

    /**
     * This advice "takes over" the update meter function and does its own version of update
     * Like the delete method, we save the previous meter data to a special backup table, to make sure we can
     * restore the data easily if we want to
     */
    void around(PowerMeter m) : MeterUpdated(m){
        System.out.println("IN DIAGNOSTICS METER UPDATED");
        init();

        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgridDiagnostics?" +
                            "user=root&password=root");

            //Here we save a backup of the change in another table, so if we want to revert back, we can revert it

            PowerMeter oldData = getMeterById(m.getID());

            String insertQuery = " insert into metersUpdatedDiag (user_id, meter_id, active, init_date, last_read, " +
                    "max_wattage, total_wattage, current_wattage)"
                    + " values (?, ?, ?, ? , ?, ?, ?, ?)";

            Object timestamp = new java.sql.Timestamp(oldData.getStartOperationDate().getTime());


            Object lastReadStamp2 = new java.sql.Timestamp(oldData.getLastReadDate().getTime());

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmtAdd = conn.prepareStatement(insertQuery);
            preparedStmtAdd.setInt(1, oldData.getCustomerID());
            preparedStmtAdd.setInt(2, oldData.getID());
            preparedStmtAdd.setBoolean(3, oldData.getIsActive());
            preparedStmtAdd.setObject(4, timestamp);
            preparedStmtAdd.setObject(5, lastReadStamp2);

            preparedStmtAdd.setInt(6, oldData.getMaxWattage());
            preparedStmtAdd.setInt(7, oldData.getTotalReading());
            preparedStmtAdd.setInt(8, oldData.readWattage());

            // execute the preparedstatement
            preparedStmtAdd.execute();

            //After we back up, we can update the meter table again

            String query = "UPDATE meter  SET user_id=? , active=?, init_date=?, last_read=?, " +
                    "max_wattage=?, total_wattage=?, current_wattage = ? Where meter_id=?";

            Object lastReadStamp = new java.sql.Timestamp(m.getLastReadDate().getTime());
            Object initStamp = new java.sql.Timestamp(m.getStartOperationDate().getTime());


            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1,m.getCustomerID());
            ps.setBoolean(2,m.getIsActive());
            ps.setObject(3,initStamp);
            ps.setObject(4,lastReadStamp);
            ps.setInt(5, m.getMaxWattage());
            ps.setInt(6, m.getTotalReading());
            ps.setInt(7, m.readWattage());
            ps.setInt(8,m.getID());

            ps.executeUpdate();

            // Do something with the Connection

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return;
    }


    /**
     * This advice "takes over" the update billing function and does its own version of it
     * Instead of calculating the customer's billing, this goes over all meter's and sets them to active.
     * It also calculates the current bill and saves it, so in this case we are adding functionality instead of
     * completely changing it
     */
    void around() : UpdateCustomerBilling(){
        System.out.println("IN DIAGNOSTICS CUSTOMER BILL UPDATED");
        //In diagnostics mode this function goes over all the meters and sets the meter to active
        //As well as calculating the bill to the customer (and adds it to the DB)
        LinkedList<Customer> customers = DBComm.getAllCustomers();
        for(Customer c : customers){
            LinkedList<PowerMeter> meters = DBComm.getAllMeterdByUserId(c.getID());
            int getTotalWattage = 0;
            for(PowerMeter m : meters){
                if(!m.getIsActive()) m.setActive();
                getTotalWattage += m.getTotalReading();
            }
            //Some standard calculation for the price to pay
            double cityTariff = DBComm.getCityTaarif(c.getAddress().getCity());
            double countryTriff = DBComm.getCountryTaarif(c.getAddress().getCountry());
            double amountToPay = getTotalWattage * cityTariff * countryTriff;
            Calendar currDate = Calendar.getInstance();
            Bill b = new Bill(c, amountToPay, currDate.getTime(), currDate.getTime());
            insertDataBill(c.getID(), (int)amountToPay, currDate.getTime(), false);
        }
        return;
    }

    /**
     * A helper function used for DB initialization
     */
    public static void init(){
        if (!initExecuted){
            initExecuted = true;
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://localhost/smartgridDiagnostics?" +
                        "user=root&password=root");
            }
            catch (Exception e){
                System.out.println("Exception in" + DBComm.class.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    /**
     * A helper function for getting a Meter from the DB via its ID
     * @param meterId The id of the meter to get
     * @return The PowerMeter from the DB
     */
    public static PowerMeter getMeterById(int meterId){
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgridDiagnostics?" +
                            "user=root&password=root");
            String query = "SELECT * FROM meter WHERE meter_id="+meterId;

            Statement st = conn.createStatement();
            ResultSet d = st.executeQuery(query );
            // Do something with the Connection

            d.next();
            int userId= d.getInt("user_id");

            boolean active = d.getBoolean("active");


            java.sql.Date init_Date = d.getDate(4);
            System.out.println("Dgdfdgdgfg");
            java.sql.Date lastRead = d.getDate(5);
            int maxWattate = d.getInt(6);
            int totalWattage = d.getInt(7);
            int currentWattage = d.getInt(8);
            System.out.println(currentWattage);
            System.out.println("userid"+userId);
            Customer cus =getCustomerById(userId);
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

    /**
     * A helper function for getting a Customer from the DB via its ID
     * @param userId The id of the customer to get
     * @return The customer object
     */
    public static Customer getCustomerById(int userId){
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgridDiagnostics?" +
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

    /**
     * A Helper function for inserting billing information to the DB
     * @param userId The id of the user for which billing needs to be inserted
     * @param currentBill The current billing amount
     * @param lastDateToPay The last date the user can pay for it
     * @param payedBool Has he payed?
     */
    public static void insertDataBill(int userId, int currentBill, java.util.Date lastDateToPay, boolean payedBool) {
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgridDiagnostics?" +
                            "user=root&password=root");
            String query = " insert into billDiagnostics (user_id, current_bill, last_date_to_pay, payed)"
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




}
