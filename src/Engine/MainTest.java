package Engine; /**
 * Created by Or Keren on 02/05/2016.
 */
import Aspects.DisconnectMeterAspect;
import Engine.DBComm;
import Engine.MeterCommunication;
import Engine.User;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class MainTest {

    public static final int METER_INTERVAL = 5;

    public static User currentUser;

    public static MeterCommunication communicator;

    public static boolean diagnosticsMode = false;

    public static void main(String[] args) {
        //LoginScreen ls = new LoginScreen();
        communicator = new MeterCommunication();
        //   System.out.println(DBComm.getCountryTaarif("maaldge"));
        Calendar currDate = new GregorianCalendar();

        //  currentUser =new Administrator();


        //Create New Customer
        Address add = new Address("Israel", "Central", "Jerusalem", "Haivrit", 12);
        Customer cs = new Customer("Omer", "Ornan", 2143, add);
        currentUser = cs;

        //Create New PowerMeter
        PowerMeter pm = new PowerMeter(23, true, currDate.getTime(), currDate.getTime(), -1, 0, 0, cs);
        PowerMeter pmToDelete = new PowerMeter(12, true, currDate.getTime(), currDate.getTime(), -1, 2200, 0, cs);

        //Add to the DB
        DBComm.addNewMeter(pm);
        DBComm.addNewMeter(pmToDelete);

        //Change the status
        pm.setInactive();
        pm.setActive();

        //Delete one of the meters
        DBComm.deletePowerMeter(pmToDelete);


        diagnosticsMode = true;

        Address orAdd = new Address("Israel", "Central", "Tel-Aviv", "University", 12);
        Customer orCs = new Customer("Or", "Keren", 2143, add);

        //Create New PowerMeter
        PowerMeter orPM = new PowerMeter(555, true, currDate.getTime(), currDate.getTime(),150, 148, 0, orCs);

        //Add to the DB
        DBComm.addNewMeter(orPM);
        orPM.setMaxWattage(850);
        DBComm.updateMeter(orPM);

                //Delete one of the meters
        DBComm.deletePowerMeter(orPM);



        //Bill b = new Bill(cs,35,null,null);
        //  BillingEngine.FirstOfMonth();
        //  BillingEngine.PayBill(cs, b);
        // Communicator.overloadFixed("dfg");


        System.out.println(pm.getIsActive());
        DBComm.updateCityTariff("test", 1);

        pm.setMaxWattage(10);
        System.out.println(pm.getIsActive());
        //DisconnectMeterAspect.disconnectAllMeters(cs);
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


    }

    public static void shutdown(){

    }
}


