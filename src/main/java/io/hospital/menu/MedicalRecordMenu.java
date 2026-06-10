package io.hospital.menu;

import io.hospital.bridge.SpringContext;
import io.hospital.enums.Severity;
import io.hospital.interfaces.IRoleMenu;
import io.hospital.model.Patient;
import io.hospital.service.MedicalRecordService;
import io.hospital.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class MedicalRecordMenu implements IRoleMenu {

    @Autowired
    private final MedicalRecordService medicalRecordService;
    private final PatientMenu patientMenu;

    public MedicalRecordMenu() {
        this.medicalRecordService = SpringContext.getBean(MedicalRecordService.class);
        this.patientMenu = new PatientMenu(new ArrayList<>());
    }

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> openNewRecord();
                case 2 -> addDiagnosis();
                case 3 -> writePrescription();
                case 4 -> recordProcedure();
                case 5 -> closeRecord();
                case 6 -> viewPatientHistory();
            }
        }
        while (choice != 0);
    }

    public void viewPatientHistory() {

    }

    public void closeRecord() {
    }

    public void recordProcedure() {
    }

    public void writePrescription() {
        Patient patient = patientMenu.listPatientsAndSelect();
        System.out.println("Enter the prescription name:");
        String name = InputHandler.getStringInput();
        System.out.println("Enter the prescription dosage:");
        double dosage = InputHandler.getDoubleInput();
        System.out.println("Enter the prescription frequency:");
        int frequency = InputHandler.getIntegerInput();
        System.out.println("Enter the prescription start date (yyyy-MM-dd):");
        LocalDate startDate = LocalDate.parse(InputHandler.getStringInput());
        System.out.println("Enter the prescription end date  (yyyy-MM-dd):");
        LocalDate endDate = LocalDate.parse(InputHandler.getStringInput());

        medicalRecordService.addPrescription(patient, name, dosage, frequency, startDate, endDate);
    }

    public void addDiagnosis() {
        Patient patient = patientMenu.listPatientsAndSelect();
        System.out.println("Enter the patient's condition:");
        String condition = InputHandler.getStringInput();
        System.out.println("Enter the patient's severity (MILD, MODERATE, SEVERE, CRITICAL):");
        Severity severity = Severity.valueOf(InputHandler.getStringInput());

        medicalRecordService.addDiagnosis(patient, condition, severity, LocalDate.now());
    }

    public void openNewRecord() {
        Patient patient = patientMenu.listPatientsAndSelect();
        System.out.println("Please enter the patient notes:");
        String notes = InputHandler.getStringInput();

        medicalRecordService.openRecord(patient, notes);
    }

    @Override
    public void printOptions() {
        System.out.println("[1] Open new record");
        System.out.println("[2] Add diagnosis");
        System.out.println("[3] Write prescription");
        System.out.println("[4] Record procedure");
        System.out.println("[5] Close record");
        System.out.println("[6] View patient history");
        System.out.println("[0] Back");
    }

}
