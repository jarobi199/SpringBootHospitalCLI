package io.hospital.alert;

import io.hospital.interfaces.AlertStrategy;

public class WardCapacityStrategy implements AlertStrategy {
    @Override
    public boolean supports(AlertContext context) {
        return context.ward() != null;
    }

    @Override
    public String evaluate(AlertContext context) {
        return (context.ward().getOccupancyPercent() >= 80) ? "ALERT: The ward occupancy is over 80%!" :  "";
    }

}
