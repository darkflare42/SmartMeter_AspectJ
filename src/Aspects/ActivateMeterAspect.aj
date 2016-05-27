package Aspects;

/**
 * This aspect deals with activating a meter (setting the status to ACTIVE)
 * Created by Or Keren on 25/05/2016.
 */
public aspect ActivateMeterAspect {

    pointcut ClientLateBillPayed();

    pointcut CityOverloadFixed();

    /**
     * This pointcut is for reactivating meters that have max wattage set - we need to reset the wattage reading
     * and also activate meters that have been inactivated because they reached max wattage
     */
    pointcut FirstOfMonthPassed();

}
