package io.hospital.service;

import io.hospital.alert.AlertContext;
import io.hospital.alert.AlertManager;
import io.hospital.enums.Severity;
import io.hospital.model.*;
import io.hospital.repository.MedicalRecordRepository;
import io.hospital.repository.PatientRepository;
import io.hospital.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;
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

    public void addProcedure(Patient patient, User doctor, String name, String outcome, String notes) {
        MedicalRecord medicalRecord = medicalRecordRepository.findByPatientIdAndOpenOrderByVisitDateDesc(patient.getId(), true).stream().findFirst().orElse(null);
        Procedure procedure = new Procedure(name, LocalDateTime.now(), doctor.getId(), outcome, notes);
        if(medicalRecord != null){
            medicalRecord.getProcedures().add(procedure);
            medicalRecordRepository.save(medicalRecord);
        }
        else
        {
            System.out.println("Patient medical record could not be found!");
        }
    }

    public void closeRecord(Patient patient) {
        MedicalRecord medicalRecord = medicalRecordRepository.findByPatientIdAndOpenOrderByVisitDateDesc(patient.getId(), true).stream().findFirst().orElse(null);
        if(medicalRecord != null){
            medicalRecord.setOpen(false);
            medicalRecordRepository.save(medicalRecord);
        }
        else
        {
            System.out.println("Patient medical record could not be found!");
        }
    }

    public void listMedicalRecords(Patient patient) {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findByPatientIdOrderByVisitDateDesc(patient.getId());
        User doctor = userRepository.findById(patient.getDoctorId()).get();
        System.out.println("PATIENT: " + patient.getFirstName() + " " + patient.getLastName());
        for(MedicalRecord medicalRecord : medicalRecords) {
            System.out.println("---------------------------------");
            System.out.println("MEDICAL RECORD ID: " + medicalRecord.getId());
            System.out.println("DOCTOR: " + doctor.getName());
            System.out.println("VISIT DATE: " + medicalRecord.getVisitDate());
            System.out.println("NOTES: " + medicalRecord.getNotes());
            System.out.println("STATUS: " + medicalRecord.isOpen());
            System.out.println("---------------------------------");
        }
    }
}
