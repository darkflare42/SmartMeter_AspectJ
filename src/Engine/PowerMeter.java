package Engine;

import java.util.Date;

/**
 * Created by Or Keren on 02/05/2016.
 */
public class PowerMeter {

    private int m_id;
    private boolean m_active;
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
        m_lastReadDate= startDate;
        m_currWattageReading = 10;
    }

    public PowerMeter(int id, Date startDate, Customer customer) {
        m_id = id;
        m_startDate = startDate;
        m_lastReadDate= startDate;
        m_currCustomer = customer;
    }

    public PowerMeter(int id,boolean active, Date init_date, Date lastRead, int maxWattage, int totalWattage, int currentWattage, Customer cust){
        this.m_id = id;
        this.m_active= active;
        this.m_startDate= init_date;
        this.m_lastReadDate = lastRead;
        this.m_maxWattage = maxWattage;
        this. m_currWattageReading = currentWattage;
        this.m_totalReading = totalWattage;
        this.m_currCustomer =cust;




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
        return m_active;
    }

    public int getMaxWattage() {
        return m_maxWattage;
    }

    public void setMaxWattage(int m_maxWattage) {
        this.m_maxWattage = m_maxWattage;
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

    public void setActive(){
        m_active = true;
    }

    public void setInactive(){
        m_active = false;
    }

    public int getCurrentWattage(){
        return m_currWattageReading;
    }

    public int getCustomerID(){
        if(m_currCustomer != null)
            return m_currCustomer.getID();
        return -1;

    }
    public void setM_currWattageReading(int wattage){
        m_currWattageReading=wattage;
    }


}
