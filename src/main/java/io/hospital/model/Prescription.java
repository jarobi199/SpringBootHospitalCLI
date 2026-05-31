package io.hospital.model;

import java.time.LocalDate;

public record Prescription(String name, double dosage, int frequency, LocalDate startDate, LocalDate endDate, boolean active ) {}
