package Aspects;

/**
 * This aspect is used to infer precedence for all the aspects in the system
 * Created by Or Keren on 01/06/2016.
 */
public aspect OrderAspects {

    /**
     * Security has to run first always, immediately after, diagnostics should run (if in diagnostics mode)
     * Updating the current bill is the next aspect in the hierarchy. After that the order does not matter as such
     * but statistics should be last
     */
    declare precedence : SecurityAspect, DiagnosticsDebuggingAspect, UpdateCurrentBillAspect, SendReportAspect,
                            ActivateMeterAspect, DisconnectMeterAspect, MeterStatusChangedAspect, StatisticsAspect;
}
