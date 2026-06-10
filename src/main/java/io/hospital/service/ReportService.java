package io.hospital.service;

import io.hospital.repository.MedicalRecordRepository;
import io.hospital.repository.PatientRepository;
import io.hospital.repository.UserRepository;
import io.hospital.util.CommandLineTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private UserRepository userRepository;

    public void hospitalSummary() {
        CommandLineTable summaryTable = new CommandLineTable();
        summaryTable.setShowVerticalLines(true);
        summaryTable.setHeaders("NUMBER OF PATIENTS ADMITTED", "NUMBER OF PATIENTS DISCHARGED", "TOTAL DOCTORS", "TOTAL WARDS","TOTAL ACTIVE PRESCRIPTIONS","NUMBER OF CRITICAL PATIENTS");
        //TODO: Add code
        summaryTable.print();
    }
}
