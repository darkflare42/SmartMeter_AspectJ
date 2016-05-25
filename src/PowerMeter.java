import java.util.Date;

/**
 * Created by Or Keren on 02/05/2016.
 */
public class PowerMeter {

    private int m_id;
    private boolean active;
    private Date m_startDate;
    private Date m_lastReadDate;
    private int m_maxWattage;
    private Customer m_currCustomer;
    private int m_currWattageReading;
    private int m_totalReading;




    public PowerMeter(int id, Date startDate) {
        m_id = id;
        m_startDate = startDate;
        m_currCustomer = null;
    }

    public PowerMeter(int id, Date startDate, Customer customer) {
        m_id = id;
        m_startDate = startDate;
        m_currCustomer = customer;
    }


    public Date getLastReadDate() {
        return m_lastReadDate;
    }

    public void setLastReadDate(Date m_lastReadDate) {
        this.m_lastReadDate = m_lastReadDate;
    }

    public int getTotalReading(){
        return m_totalReading;
    }

    public Date getStartOperationDate() {
        return m_startDate;
    }

    public int getID() {
        return m_id;
    }

    public boolean getIsActive(){
        return active;
    }

    public int getMaxWattage() {
        return m_maxWattage;
    }

    public void setMaxWattage(int m_maxWattage) {
        this.m_maxWattage = m_maxWattage;
    }

    public int getCostumerID(){
        return m_currCustomer.getID();
    }

    public Address getCurrentLocation(){
        return m_currCustomer.getAddress();
    }

    public void resetMaxWattage(){
        setMaxWattage(0);
    }

    /**
     * MOCK-UP of the actual reading function. This function just gets the current reading and adds one to it
     * @return The current wattage of the meter
     */
    public int readWattage(){
        return ++m_currWattageReading;
    }


}
