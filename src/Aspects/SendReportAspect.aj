package Aspects;

/**
 * Created by Or Keren on 21/05/2016.
 */
public aspect SendReportAspect {

    pointcut UserLeft();

    pointcut UserRequestedReport(); //TODO: Not really sure this is needed

    pointcut FirstOfMonth(); //TODO: Not sure this is needed
}
