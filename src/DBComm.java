import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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


}
