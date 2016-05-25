/**
 * Created by Or Keren on 21/05/2016.
 */
public aspect UpdateCurrentBillAspect {

    pointcut MeterReadingChanged();

    pointcut CountryTariffChanged();

    pointcut CityTariffChanged();


}
