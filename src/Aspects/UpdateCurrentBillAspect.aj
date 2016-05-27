package Aspects;

/**
 *  This aspect deals with updating the current billing of all the customers in the system
 *
 * Created by Or Keren on 21/05/2016.
 */
public aspect UpdateCurrentBillAspect {

    /**
     * Whenever a meter's reading is changed, the value inside the billing table should be updated to reflect this
     */
    pointcut MeterReadingChanged();

    /**
     * Whenever a country's tariff changes - the
     */
    pointcut CountryTariffChanged();

    /**
     * This pointcut should change the billing information if the customer has not payed his bill up to certain points
     */
    pointcut FineCustomer();

    pointcut CityTariffChanged();


}
