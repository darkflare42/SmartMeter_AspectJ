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


    public Bill(Customer customer, double value, Date cutOffDate, Date issuedDate){
        m_customer = customer;
        m_price = value;
        m_cutOffDate = cutOffDate;
        m_issuedDate = issuedDate;
    }

    public void updateFine(int value){
        m_price = value;
    }

    public Date getIssuedDate(){
        return m_issuedDate;
    }
}
