package Aspects;

/**
 * Created by Or Keren on 01/06/2016.
 */
public aspect OrderAspects {
    declare precedence : SecurityAspect, DiagnosticsDebuggingAspect, UpdateCurrentBillAspect, SendReportAspect;
}
