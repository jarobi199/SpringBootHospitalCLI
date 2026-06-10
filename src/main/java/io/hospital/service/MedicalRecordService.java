package io.hospital.service;

import io.hospital.alert.AlertContext;
import io.hospital.alert.AlertManager;
import io.hospital.enums.Severity;
import io.hospital.model.*;
import io.hospital.repository.MedicalRecordRepository;
import io.hospital.repository.PatientRepository;
import io.hospital.repository.UserRepository;
import io.hospital.util.CommandLineTable;
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

    public void addDiagnosis(Patient patient, String diagnosisDescription, Severity severity, LocalDate diagnosisDate) {
        Diagnosis diagnosis = new Diagnosis(diagnosisDescription, severity, diagnosisDate);
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
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("MEDICAL RECORD ID: " + medicalRecord.getId());
            System.out.println("DOCTOR: " + doctor.getName());
            System.out.println("VISIT DATE: " + medicalRecord.getVisitDate());
            System.out.println("NOTES: " + medicalRecord.getNotes());
            System.out.println("STATUS: " + (medicalRecord.isOpen() ? "OPEN" : "CLOSED"));
            System.out.println();

            List<Diagnosis> diagnoses = medicalRecord.getDiagnoses();
            List<Prescription> prescriptions = medicalRecord.getPrescriptions();
            List<Procedure> procedures  = medicalRecord.getProcedures();

            System.out.println("DIAGNOSES");
            CommandLineTable diagnosisTable = new CommandLineTable();
            diagnosisTable.setShowVerticalLines(true);
            diagnosisTable.setHeaders("CONDITION", "SEVERITY", "DIAGNOSIS DATE");
            for (Diagnosis diagnosis : diagnoses) {
                diagnosisTable.addRow(diagnosis.condition(), diagnosis.severity().name(), diagnosis.diagnosisDate().toString());
            }
            diagnosisTable.print();
            System.out.println();

            System.out.println("PRESCRIPTIONS");
            CommandLineTable prescriptionTable = new CommandLineTable();
            prescriptionTable.setShowVerticalLines(true);
            prescriptionTable.setHeaders("NAME", "DOSAGE", "FREQUENCY","START DATE","END DATE","ALERT");
            for (Prescription prescription : prescriptions) {
                String alert = alertManager.evaluate(new AlertContext(null, null, prescription, null));
                prescriptionTable.addRow(prescription.name(), String.valueOf(prescription.dosage()), String.valueOf(prescription.frequency()),
                        prescription.startDate().toString(), prescription.endDate().toString(), alert);
            }
            prescriptionTable.print();
            System.out.println();
            alertManager.evaluate(new AlertContext(null, null, prescriptions.getFirst(), null));

            System.out.println("PROCEDURES");
            CommandLineTable procedureTable = new CommandLineTable();
            procedureTable.setShowVerticalLines(true);
            procedureTable.setHeaders("NAME", "PERFORMANCE DATE", "DOCTOR", "OUTCOME","NOTES");
            for (Procedure procedure : procedures) {
                User procedureDoctor = userRepository.findById(procedure.doctorId()).get();
                procedureTable.addRow(procedure.name(), procedure.performanceDate().toString(), procedureDoctor.getName(), procedure.outcome(), procedure.notes());
            }
            diagnosisTable.print();
            System.out.println();
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
        }
    }
}
