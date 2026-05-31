package io.hospital.model;

import io.hospital.enums.Severity;

import java.time.LocalDate;

public record Diagnosis(String condition, Severity severity, LocalDate diagnosisDate) {}
