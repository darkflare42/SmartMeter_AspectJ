package Engine;

import java.util.Date;

/**
 * Created by Or Keren on 27/05/2016.
 */
public class Bill {

    private int m_id;
    private Customer m_customer;
    private double m_price;
    private Date m_cutOffDate;
    private Date m_issuedDate;
    private boolean m_payed;


    public Bill(Customer customer, double value, Date cutOffDate, Date issuedDate, boolean payed){
        m_customer = customer;
        m_price = value;
        m_cutOffDate = cutOffDate;
        m_issuedDate = issuedDate;
        m_id = -1;
        m_payed = payed;
    }

    public Bill(Customer customer, double value, Date cutOffDate, Date issuedDate){
        this(customer, value, cutOffDate, issuedDate, false);
    }


    public void updateFine(int value){
        m_price += value;
    }

    public Date getIssuedDate(){
        return m_issuedDate;
    }

    public Date getCutoffDate() {return m_cutOffDate;}

    public Customer getCustomer(){return m_customer;}

    public double getPrice() {return m_price;}

    public void setPrice(double price){m_price = price;}

    public boolean getPayed(){return m_payed;}

    public void updateCityTariff(int newTariff){
        m_price = m_price * newTariff * 0.02;
    }

    public void updateCountryTariff(int newTariff){
        m_price = m_price * newTariff * 0.2;
    }


}
