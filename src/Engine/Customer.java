package Engine;

import GUI.CustomerMainMenu;

/**
 * Created by Or Keren on 02/05/2016.
 */
public class Customer extends User {

    private String m_fname;
    private String m_lname;
    private int m_id;
    private Address m_address;

    public Customer(String firstName, String surname, int id, Address currentAddress){
        m_fname = firstName;
        m_lname = surname;
        m_id = id;
        m_address = currentAddress;
    }

    public String getFirstName() {
        return m_fname;
    }

    public String getSurname() {
        return m_lname;
    }

    public int getID() {
        return m_id;
    }

    public Address getAddress() {
        return m_address;
    }

    @Override
    public void openMainMenu(){
        CustomerMainMenu m = new CustomerMainMenu();
    }

}
