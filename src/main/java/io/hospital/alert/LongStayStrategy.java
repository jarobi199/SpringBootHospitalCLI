package io.hospital.alert;

import io.hospital.interfaces.AlertStrategy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LongStayStrategy implements AlertStrategy {

    public static final int ADMISSION_LIMIT = 30;

    @Override
    public boolean supports(AlertContext context) {
        return context.patient() != null;
    }

    @Override
    public String evaluate(AlertContext context) {
        String result = "";
        if((context.patient().getDischargeDate() == null) && ((ChronoUnit.DAYS.between(context.patient().getAdmissionDate(), LocalDate.now()) > ADMISSION_LIMIT))) {
            result = "ALERT: The patient has been admitted for more than " + ADMISSION_LIMIT + " days!";
        }
        return result;
    }
}
