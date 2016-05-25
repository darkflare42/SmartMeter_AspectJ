package Engine;

import java.util.LinkedList;

/** This class is a mockup of the communication application.
 * It has a local list of meters, which it iterates over every INTERVAL seconds
 * And reads the meter's wattage. After reading the program updates the DB only.
 * Created by Or Keren on 05/05/2016.
 */
public class MeterCommunication implements Runnable{

    LinkedList<PowerMeter> m_currMeters;

    /**
     * The constructor starts by getting all the current meters from the DB
     */
    public MeterCommunication(){
        //m_currMeters = new LinkedList<>(Engine.DBComm.getAllActiveMeters());
    }


    /**
     * Handles the meter communication
     */
    @Override
    public void run() {

        for(PowerMeter m : m_currMeters){
            int watt = m.readWattage();
            //Engine.DBComm.updateMeterReading(m.getID, watt);
            //TODO: Add function to DBCOMM to update the current reading of a meter (id & reading)
        }

        //Go over the list of meters and call "readMeter" function on it
        try {
            Thread.sleep(Engine.MainTest.METER_INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * This function should be called whenever a new meter is added to the system OR when it becomes ACTIVE
     * @param m
     */
    public void addMeter(PowerMeter m){
        m_currMeters.add(m);
    }

    /**
     * This function should be called whenever a meter is deleted from the system OR when it becomes INACTIVE
     * @param m
     */
    public void removeMeter(PowerMeter m){
        m_currMeters.remove(m); //TODO: Check that this works, maybe we need to search via ID
    }

    public static void main(String[] arg){
        System.out.print("hello world");

    }
}


