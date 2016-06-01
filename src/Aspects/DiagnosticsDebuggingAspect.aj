package Aspects;

import Engine.PowerMeter;
import Engine.DBComm;

/** This aspect is used to allow using the system in a sandbox
 * every major operation that the user can do will not be saved in the static DB, but in a temporary DB
 * Created by Or Keren on 27/05/2016.
 */
public aspect DiagnosticsDebuggingAspect {


    pointcut NewMeterAdded(PowerMeter m): execution(* DBComm.addNewMeter(PowerMeter)) &&
                                            args(m) && if(diagnosticsMode);

    pointcut MeterRemoved(PowerMeter m): execution(* DBComm.deletePowerMeter(PowerMeter)) &&
                                            args(m) && if(diagnosticsMode);

    pointcut MeterUpdated(PowerMeter m) : execution(* DBComm.updateMeter(PowerMeter)) &&
                                            args(m) && if(diagnosticsMode);

    pointcut UpdateCustomerBilling() : execution(* BillingEngine.checkMonthlyBilling()) &&
                                            if(diagnosticsMode);


    void around(PowerMeter m) : NewMeterAdded(m){
        int x;
    }

    void around(PowerMeter m) : MeterRemoved(m){
        int x;
    }

    void around(PowerMeter m) : MeterUpdated(m){
        int x;
    }

    void around() : UpdateCustomerBilling(){
        int x;
    }


}
