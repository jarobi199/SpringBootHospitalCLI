package io.hospital.service;

import io.hospital.alert.AlertContext;
import io.hospital.alert.AlertManager;
import io.hospital.authentication.SessionContext;
import io.hospital.enums.Gender;
import io.hospital.enums.Status;
import io.hospital.model.*;
import io.hospital.repository.MedicalRecordRepository;
import io.hospital.repository.PatientRepository;
import io.hospital.repository.UserRepository;
import io.hospital.repository.WardRepository;
import io.hospital.util.CommandLineTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AlertManager alertManager;

    public void listPatients(String doctorId) {
        System.out.println("DR. " + SessionContext.getCurrentUser().getName().toUpperCase() + "'S PATIENT LIST:");
        List<Patient> patients = patientRepository.findByDoctorId(doctorId);
        displayPatients(patients);
    }

    public void listPatients() {
        List<Patient> patients = patientRepository.findAll();
        displayPatients(patients);
    }

    private void displayPatients(List<Patient> patients) {
        CommandLineTable table = new CommandLineTable();
        table.setShowVerticalLines(true);
        table.setHeaders("FIRST NAME", "LAST NAME", "GENDER", "DATE OF BIRTH", "CONTACT NUMBER", "STATUS", "ADMISSION DATE", "DISCHARGE DATE" );
        for (Patient patient : patients) {
            table.addRow(patient.getFirstName(), patient.getLastName(), patient.getGender().name(), patient.getDateOfBirth().toString(),
                    String.valueOf(patient.getContactNumber()), patient.getStatus().name(), patient.getAdmissionDate().toString(), (patient.getDischargeDate() == null) ? "N/A" : patient.getDischargeDate().toString());
        }
        table.print();
    }

    public void admitPatient(String firstName, String lastName, Gender gender, int phoneNumber,
                             LocalDate birthDate, LocalDateTime admissionDate, Status status, User doctor, Ward ward) {
        Patient patient = new Patient(ward.getId(), doctor.getId(), birthDate, gender, firstName, lastName, birthDate, phoneNumber, status, null, admissionDate);
        patientRepository.save(patient);

        ward.setCurrentOccupancy(ward.getCurrentOccupancy() + 1);
        wardRepository.save(ward);

        String alert = alertManager.evaluate(new AlertContext(null, ward, null, null));
        System.out.println(alert);
    }

    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    public void assignDoctor(Patient patient, User doctor) {
        patient.setDoctorId(doctor.getId());
        patientRepository.save(patient);
        System.out.println("Patient assigned to doctor: Dr. " + doctor.getName());
    }

    public void dischargePatient(Patient patient) {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.
                findByPatientIdAndOpenOrderByVisitDateDesc(patient.getId(), true);
        if (medicalRecords.isEmpty()) {
            System.out.println("There are no records available for this patient and therefore cannot be discharged");
        }
        else {
            medicalRecords.forEach(medicalRecord -> medicalRecord.setOpen(false));
            Optional<Ward> optionalWard =  wardRepository.findById(patient.getWardId());
            optionalWard.ifPresent(ward -> {
                ward.setCurrentOccupancy(ward.getCurrentOccupancy() - 1);
                wardRepository.save(ward);
            });
            patient.setDischargeDate(LocalDateTime.now());
            patient.setStatus(Status.DISCHARGED);
            patientRepository.save(patient);
        }
    }

    public void viewPatient(Patient patient) {
        Optional<Ward> optionalWard = wardRepository.findById(patient.getWardId());
        Optional<User> optionalUser = userRepository.findById(patient.getDoctorId());
        String wardName = (optionalWard.isEmpty()) ? "N/A" : optionalWard.get().getName();
        String doctorName = (optionalUser.isEmpty()) ? "N/A" : optionalUser.get().getName();

        System.out.println("WARD: " + wardName + " | DOCTOR: " + doctorName);
        displayPatients(List.of(patient));
        System.out.println();

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findByPatientIdOrderByVisitDateDesc(patient.getId());
        List<Diagnosis> diagnoses = new ArrayList<>();
        List<Prescription> prescriptions = new ArrayList<>();
        List<Procedure> procedures  = new ArrayList<>();
        for (MedicalRecord medicalRecord : medicalRecords) {
            diagnoses.addAll(medicalRecord.getDiagnoses());
            prescriptions.addAll(medicalRecord.getPrescriptions());
            procedures.addAll(medicalRecord.getProcedures());
        }

        if(!diagnoses.isEmpty()){
            System.out.println("DIAGNOSES");
            CommandLineTable diagnosisTable = new CommandLineTable();
            diagnosisTable.setShowVerticalLines(true);
            diagnosisTable.setHeaders("CONDITION", "SEVERITY", "DIAGNOSIS DATE");
            for (Diagnosis diagnosis : diagnoses) {
                diagnosisTable.addRow(diagnosis.condition(), diagnosis.severity().name(), diagnosis.diagnosisDate().toString());
            }
            diagnosisTable.print();
            System.out.println();
        }

        if(!prescriptions.isEmpty()) {
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
        }

        if(!procedures.isEmpty()) {
            System.out.println("PROCEDURES");
            CommandLineTable procedureTable = new CommandLineTable();
            procedureTable.setShowVerticalLines(true);
            procedureTable.setHeaders("NAME", "PERFORMANCE DATE", "DOCTOR", "OUTCOME","NOTES");
            for (Procedure procedure : procedures) {
                User doctor = userRepository.findById(procedure.doctorId()).get();
                procedureTable.addRow(procedure.name(), procedure.performanceDate().toString(), doctor.getName(), procedure.outcome(), procedure.notes());
            }
            procedureTable.print();
            System.out.println();
        }

    }
}
