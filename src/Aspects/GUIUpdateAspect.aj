package Aspects;

/**
 * Created by Or Keren on 21/05/2016.
 * This aspect changes the GUI to reflect that an update to a meter's reading has occurred
 */
public aspect GUIUpdateAspect {

    pointcut MeterReadingChanged();

    pointcut MeterStatusChanged();

}
