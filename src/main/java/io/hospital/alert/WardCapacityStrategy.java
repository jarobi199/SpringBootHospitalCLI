package io.hospital.alert;

import io.hospital.interfaces.AlertStrategy;

public class WardCapacityStrategy implements AlertStrategy {
    @Override
    public boolean supports(AlertContext context) {
        return context.ward() != null;
    }

    @Override
    public void evaluate(AlertContext context) {
        if(context.ward().getOccupancyPercent() >= 80) {
            System.out.println("ALERT: The ward occupancy is over 80%!");
        }
    }

}
