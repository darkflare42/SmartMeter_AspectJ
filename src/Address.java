/**
 * Created by Or Keren on 02/05/2016.
 */
public class Address {

    private String m_country;
    private String m_district;
    private String m_city;
    private String m_street;
    private int m_houseNum;

    public Address(String country, String district, String city, String streetName, int houseNumber){
        m_country = country;
        m_district = district;
        m_city = city;
        m_street = streetName;
        m_houseNum = houseNumber;
    }

    public Address(String country, String city, String streetName, int houseNumber){
        this(country, "N\\A", city, streetName, houseNumber);
    }


    public String getCountry() {
        return m_country;
    }

    public String getDistrict() {
        return m_district;
    }

    public String getCity() {
        return m_city;
    }

    public String getStreetName() {
        return m_street;
    }

    public int getHouseNumber() {
        return m_houseNum;
    }

    @Override
    public String toString() {
        return m_houseNum + " " + m_street + ", " + m_city + "\r\n" +
                (m_district.equals("N\\A")? "": m_district + "\r\n") + m_country;
    }
}
