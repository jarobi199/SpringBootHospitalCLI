package io.hospital.alert;

import io.hospital.model.Diagnosis;
import io.hospital.model.Patient;
import io.hospital.model.Prescription;
import io.hospital.model.Ward;

public record AlertContext (Patient patient, Ward ward, Prescription prescription, Diagnosis diagnosis){}

