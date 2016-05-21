/**
 * Created by Omer on 18/05/2016.
 */

import java.sql.*;

public class trytoconnect {


    // Notice, do not import com.mysql.jdbc.*
// or you will have problems!
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            insertDataLogIn(324234,"Dgdg","dfg");
            Statement st =  conn.createStatement();
            ResultSet d=st.executeQuery("SELECT * FROM  login");
            while(d.next()){
                System.out.print(d.getString(1)+" "+d.getString(2));
            }

            // Do something with the Connection
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendoreError: " + ex.getErrorCode());
        }

    }

    public static void insertDataLogIn(int userID, String userName, String password){

        Connection conn = null;
        try {
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/smartgrid?" +
                            "user=root&password=root");
            // create a sql date object so we can use it in our INSERT statement


            // the mysql insert statement
            String query = " insert into login (user_id, user_name, password)"
                    + " values (?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt (1, userID);
            preparedStmt.setString (2, userName);
            preparedStmt.setString   (3, password);
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


