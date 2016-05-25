package Aspects;

/**
 * Created by Or Keren on 21/05/2016.
 */
public aspect StatisticsAspect {

    pointcut UserJoined();

    pointcut NewMeterConnected();

    pointcut BillingFinished();
}
