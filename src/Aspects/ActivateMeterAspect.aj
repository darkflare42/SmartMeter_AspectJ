package Aspects;

/**
 * This aspect deals with activating a meter (setting the status to ACTIVE)
 * Created by Or Keren on 25/05/2016.
 */
public aspect ActivateMeterAspect {

    pointcut ClientLateBillPayed();

    pointcut CityOverloadFixed();

    pointcut FirstOfMonthPassed();

}
