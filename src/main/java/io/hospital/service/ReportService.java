package io.hospital.service;

import io.hospital.alert.AlertContext;
import io.hospital.alert.AlertManager;
import io.hospital.enums.Role;
import io.hospital.enums.Severity;
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
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private AlertManager alertManager;

    public void hospitalSummary() {
        int admittedPatients = patientRepository.findByAdmissionDateIsNotNullAndDischargeDateIsNull().size();
        int dischargedPatients = patientRepository.findByAdmissionDateIsNotNullAndDischargeDateIsNotNull().size();
        int totalDoctors = userRepository.findByRole(Role.DOCTOR).size();
        int totalWards = Math.toIntExact(wardRepository.count());
        Set<String> criticalPatientIds = new HashSet<>();
        for (MedicalRecord medicalRecord : medicalRecordRepository.findAll()) {
            for(Diagnosis diagnosis : medicalRecord.getDiagnoses()) {
                if(Severity.CRITICAL.equals(diagnosis.severity())) {
                    criticalPatientIds.add(medicalRecord.getPatientId());
                }
            }
        }
        int criticalPatients = criticalPatientIds.size();

        long totalActivePrescriptions = medicalRecordRepository.findAll().stream()
                .flatMap(record -> record.getPrescriptions().stream())
                .filter(Prescription::active)
                .count();

        CommandLineTable summaryTable = new CommandLineTable();
        summaryTable.setShowVerticalLines(true);
        summaryTable.setHeaders("PATIENTS ADMITTED", "PATIENTS DISCHARGED", "TOTAL DOCTORS", "TOTAL WARDS","TOTAL ACTIVE PRESCRIPTIONS","CRITICAL PATIENTS");
        summaryTable.addRow(String.valueOf(admittedPatients), String.valueOf(dischargedPatients),
                String.valueOf(totalDoctors), String.valueOf(totalWards), String.valueOf(totalActivePrescriptions), String.valueOf(criticalPatients));
        summaryTable.print();
    }

    public void listWardOccupancy() {
        List<Ward> wards = wardRepository.findAll().stream().sorted(Comparator.comparing(Ward::getOccupancyPercent).reversed()).toList();
        CommandLineTable table = new CommandLineTable();
        table.setHeaders("NAME", "WARD TYPE", "TOTAL BEDS","CURRENT OCCUPANCY","OCCUPANCY PERCENTAGE","LONG STAY PATIENTS");
        table.setShowVerticalLines(true);
        wards.forEach(ward -> {
            int occupancyPercentage = ward.getOccupancyPercent();
            int filled = Math.round(occupancyPercentage / 10.0f);
            String alert =occupancyPercentage >= 80 ? "This ward is over 80% capacity! ⚠" : "";
            String bar = "▓".repeat(filled) + "░".repeat(10 - filled) + " " + alert;
            List<Patient> patients = patientRepository.findByWardId(ward.getId());
            String longStay = null;
            for(Patient patient : patients) {
                longStay = alertManager.evaluate(new AlertContext(patient, null, null, null));
            }
            table.addRow(ward.getName(), ward.getWardType().name(), String.valueOf(ward.getTotalBeds()), String.valueOf(ward.getCurrentOccupancy()), bar, longStay);
        });
        table.print();
    }

    public void listActivePrescriptions() {
        List<MedicalRecord> medicalRecords =  medicalRecordRepository.findAll();
        List<Prescription> prescriptions = new ArrayList<>();
        for (MedicalRecord medicalRecord : medicalRecords) {
            prescriptions.addAll(medicalRecord.getPrescriptions());
        }

        if(!prescriptions.isEmpty()) {
            prescriptions = prescriptions.stream().sorted(Comparator.comparing(Prescription::endDate).reversed()).toList();
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
        }
    }

    public void listLongStayPatients() {
        Map<Patient, Long> longStayMap = new HashMap<>();
        List<Patient> patients = patientRepository.findAll().stream()
                .filter(patient -> {
                    LocalDateTime endDateTime = (patient.getDischargeDate() == null) ? LocalDateTime.now() : patient.getDischargeDate();
                    long admittedDays = ChronoUnit.DAYS.between(patient.getAdmissionDate(), endDateTime);
                    boolean longStay = (admittedDays >= 30);
                    if (longStay) {
                        longStayMap.put(patient, admittedDays);
                    }
                    return longStay;
                })
                .sorted(Comparator.comparing(Patient::getAdmissionDate).reversed()).toList();

        CommandLineTable patientTable = new CommandLineTable();
        patientTable.setShowVerticalLines(true);
        patientTable.setHeaders("NAME", "WARD", "DOCTOR", "DAYS ADMITTED", "LATEST DIAGNOSIS");
        for (Patient patient : patients) {
            Ward ward = wardRepository.findById(patient.getWardId()).get();
            User doctor = userRepository.findById(patient.getDoctorId()).get();
            long daysAdmitted = longStayMap.get(patient);
            patientTable.addRow(patient.getFirstName() + " " + patient.getLastName(), doctor.getName(), ward.getName(), daysAdmitted, );
        }
        patientTable.print();
    }

}
