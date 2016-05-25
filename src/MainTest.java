/**
 * Created by Or Keren on 02/05/2016.
 */
import Engine.DBComm;
import Engine.MeterCommunication;
import Engine.User;


public class MainTest {

    public static final int METER_INTERVAL = 5;

    public static User currentUser;

    public static void main(String[] args) {
        //LoginScreen ls = new LoginScreen();
        DBComm.init();
        new Thread(new MeterCommunication()).start();

    }
}


