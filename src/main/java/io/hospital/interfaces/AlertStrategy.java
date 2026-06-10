package io.hospital.interfaces;

import io.hospital.alert.AlertContext;

public interface AlertStrategy {
    boolean supports(AlertContext context);
    String evaluate(AlertContext context);
}