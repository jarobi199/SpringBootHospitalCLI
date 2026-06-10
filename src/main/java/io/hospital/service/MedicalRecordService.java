package io.hospital.service;

import io.hospital.alert.AlertContext;
import io.hospital.alert.AlertManager;
import io.hospital.enums.Severity;
import io.hospital.model.Diagnosis;
import io.hospital.model.MedicalRecord;
import io.hospital.model.Patient;
import io.hospital.model.Prescription;
import io.hospital.repository.MedicalRecordRepository;
import io.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private AlertManager alertManager;


    public void openRecord(Patient patient, String notes) {
        if(!medicalRecordRepository.findByPatientIdAndOpenOrderByVisitDateDesc(patient.getId(), true).isEmpty()) {
            System.out.println("Patient medical record has already been opened!");
        }
        else
        {
            MedicalRecord medicalRecord = new MedicalRecord(patient.getId(), patient.getDoctorId(), LocalDateTime.now(), notes);
            medicalRecordRepository.save(medicalRecord);
            System.out.println("Patient medical record has been opened.");
        }
    }

    public void addDiagnosis(Patient patient, String condition, Severity severity, LocalDate diagnosisDate) {
        Diagnosis diagnosis = new Diagnosis(condition, severity, diagnosisDate);
        MedicalRecord  medicalRecord = medicalRecordRepository.findByPatientIdAndOpenOrderByVisitDateDesc(patient.getId(), true).stream().findFirst().orElse(null);
        if(medicalRecord != null){
            medicalRecord.getDiagnoses().add(diagnosis);
            medicalRecordRepository.save(medicalRecord);

            String alert = alertManager.evaluate(new AlertContext(null, null, null, diagnosis));
            System.out.println(alert);
        }
        else
        {
            System.out.println("Patient medical record could not be found!");
        }
    }

    public void addPrescription(Patient patient, String name, double dosage, int frequency, LocalDate startDate, LocalDate endDate) {
        Prescription prescription = new Prescription(name, dosage, frequency, startDate, endDate, true);
        MedicalRecord  medicalRecord = medicalRecordRepository.findByPatientIdAndOpenOrderByVisitDateDesc(patient.getId(), true).stream().findFirst().orElse(null);
        if(medicalRecord != null){
            medicalRecord.getPrescriptions().add(prescription);
            medicalRecordRepository.save(medicalRecord);
        }
        else
        {
            System.out.println("Patient medical record could not be found!");
        }
    }
}
