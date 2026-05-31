package io.hospital.interfaces;

import io.hospital.alert.AlertContext;

public interface AlertStrategy {
    boolean supports(AlertContext context);
    void evaluate(AlertContext context);
}