/**
 * Created by Or Keren on 21/05/2016.
 */
public aspect DisconnectMeterAspect {


    pointcut ClientDisconnected();

    pointcut ClientBillNotPayed();

    pointcut MeterWattageMaxed();

    pointcut MeterWattageOverload();

}
