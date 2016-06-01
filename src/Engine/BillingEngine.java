package Engine;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeUnit.*;

/**
 * Created by Or Keren on 26/05/2016.
 */
public class BillingEngine {

    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public static void init(){
        final Runnable everyFirstOfMonth = new Runnable(){
            public void run(){checkMonthlyBilling();}
        };
        //For testing purposes the scheduler runs every day, rather than every month
        final ScheduledFuture<?> checkMonthlyHandler = scheduler.scheduleAtFixedRate(everyFirstOfMonth, 1, 1, TimeUnit.DAYS);
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                checkMonthlyHandler.cancel(false);
            }
        }, 40, TimeUnit.DAYS);
    }



    public static void checkMonthlyBilling(){
        //Go over every customer in the DB, and then over each and every active meter that he has
        LinkedList<Customer> customers = DBComm.getAllCustomers();
        for(Customer c : customers){
            calculateCurrentBill(c);
        }


    }

    public static void calculateCurrentBill(Customer c){
        LinkedList<PowerMeter> meters = DBComm.getAllMeterdByUserId(c.getID());
        int getTotalWattage = 0;
        for(PowerMeter m : meters){
            if(m.getIsActive()){  //Run over only active meters
                getTotalWattage += m.getTotalReading();
            }
        }
        //Some standard calculation for the price to pay
        double cityTariff = DBComm.getCityTaarif(c.getAddress().getCity());
        double countryTriff = DBComm.getCountryTaarif(c.getAddress().getCountry());
        double amountToPay = getTotalWattage * cityTariff * countryTriff;
        Calendar currDate = Calendar.getInstance();
        Bill b = new Bill(c, amountToPay, currDate.getTime(), currDate.getTime());
        DBComm.insertDataBill(c.getID(), (int)amountToPay, currDate.getTime(), false);
    }



    public static void sendBillingReportByMail(int bill, User user){
        //Mock function. In reality this calls an external report engine
    }

    public static void updateCurrentCustomerBill(int customerID, int wattage){
        Bill b = DBComm.getBillByCustomerID(customerID);
        double price = b.getPrice();
        price += (wattage * (DBComm.getCityTaarif(b.getCustomer().getAddress().getCity())));
        b.setPrice(price);
        DBComm.updateBill(b);
    }

    public static void PayBill(Customer c, Bill b){
        //Mock function - in real life this would be an asynchronous function that waits for
        //a payment authorization from the credit card company
    }
    public static void PayBill(Customer c, Bill b){}


    public static void FirstOfMonth(){
        //Mock function. In reailty this call to report about function that have to be activate
    }
}
