package Engine;

import java.util.Date;

/**
 * Created by Or Keren on 27/05/2016.
 */
public class Bill {

    private int m_id;
    private Customer m_customer;
    private int m_price;
    private Date m_cutOffDate;
    private Date m_issuedDate;


    public Bill(){

    }

    public void updateFine(int value){
        m_price = value;
    }

    public Date getIssuedDate(){
        return m_issuedDate;
    }
}
