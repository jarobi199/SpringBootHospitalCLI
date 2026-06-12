package io.hospital.alert;

import io.hospital.enums.Severity;
import io.hospital.interfaces.AlertStrategy;

public class CriticalDiagnosisStrategy implements AlertStrategy {
    @Override
    public boolean supports(AlertContext context) {
        return context.diagnosis() != null;
    }

    @Override
    public String evaluate(AlertContext context) {
        return (Severity.CRITICAL.equals(context.diagnosis().severity())) ? "ALERT: The diagnosis is CRITICAL!" :  "";
    }
}
