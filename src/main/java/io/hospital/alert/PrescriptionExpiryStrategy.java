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
    public void evaluate(AlertContext context) {
        if(ChronoUnit.DAYS.between(LocalDate.now(), context.prescription().endDate()) <= EXPIRY_WINDOW) {
            System.out.println("ALERT: The prescription is about to expire within the next " + EXPIRY_WINDOW + " days!");
        }
    }
}
