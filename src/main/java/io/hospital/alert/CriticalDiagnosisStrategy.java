package io.hospital.alert;

import io.hospital.enums.Severity;
import io.hospital.interfaces.AlertStrategy;

public class CriticalDiagnosisStrategy implements AlertStrategy {
    @Override
    public boolean supports(AlertContext context) {
        return context.diagnosis() != null;
    }

    @Override
    public void evaluate(AlertContext context) {
        if(Severity.CRITICAL.equals(context.diagnosis().severity())){
            System.out.println("ALERT: The diagnosis is CRITICAL!");
        }
    }
}
