package io.hospital.service;

import io.hospital.enums.Role;
import io.hospital.enums.Severity;
import io.hospital.model.Diagnosis;
import io.hospital.model.MedicalRecord;
import io.hospital.model.Prescription;
import io.hospital.repository.MedicalRecordRepository;
import io.hospital.repository.PatientRepository;
import io.hospital.repository.UserRepository;
import io.hospital.repository.WardRepository;
import io.hospital.util.CommandLineTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

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
}
