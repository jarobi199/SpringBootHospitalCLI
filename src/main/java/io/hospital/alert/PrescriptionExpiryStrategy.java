package io.hospital.alert;

import io.hospital.interfaces.AlertStrategy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PrescriptionExpiryStrategy implements AlertStrategy {

    public static final int EXPIRY_WINDOW = 3;

    @Override
    public boolean supports(AlertContext context) {
        return context.prescription() != null;
    }

    @Override
    public String evaluate(AlertContext context) {
        String result = "";
        if(ChronoUnit.DAYS.between(LocalDate.now(), context.prescription().endDate()) <= EXPIRY_WINDOW) {
            result = "ALERT: The prescription is about to expire within the next " + EXPIRY_WINDOW + " days!";
        }
        return result;
    }
}
