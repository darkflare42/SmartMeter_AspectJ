package Engine; /**
 * Created by Or Keren on 02/05/2016.
 */
import Engine.DBComm;
import Engine.MeterCommunication;
import Engine.User;

import java.util.Date;


public class MainTest {

    public static final int METER_INTERVAL = 5;

    public static User currentUser;

    public static MeterCommunication communicator;

    public static boolean diagnosticsMode = false;

    public static void main(String[] args) {
        //LoginScreen ls = new LoginScreen();
        //communicator = new MeterCommunication();
     //   System.out.println(DBComm.getCountryTaarif("maaldge"));









      //  currentUser =new Administrator();
        Address add = new Address("maaldge","maasfsdfldge","maale","o",234);
        Customer cs= new Customer("omer","Ornan",2143,add);
        currentUser = cs;
        //Bill b = new Bill(cs,35,null,null);
      //  BillingEngine.FirstOfMonth();
      //  BillingEngine.PayBill(cs, b);
       // Communicator.overloadFixed("dfg");

        PowerMeter pm = new PowerMeter(23,false,null,null,5,5,5,cs);
        System.out.println(pm.getIsActive());
        pm.setMaxWattage(10);
        System.out.println(pm.getIsActive());
       // BillingEngine.checkMonthlyBilling();
        //DBComm.deleteCustomer(cs);
       // diagnosticsMode = true;
        //DBComm.deletePowerMeter(m);
//        m.readWattage();
        //DBComm.addNewMeter(m);
//        DBComm.getMeterById(23);


//        Address d=new Address("234","234","234","234",2);
//        Customer n= new Customer("sfd","@43",234,d);
//        DBComm.getAllMeterdByUserId(1);
//       DBComm.insertDataBill(5,5,new Date(),true);

        //TODO: Have to make sure that somewhere after the communicator we read all the values


    }
}


