package io.hospital.menu;

import io.hospital.bridge.SpringContext;
import io.hospital.interfaces.IRoleMenu;
import io.hospital.service.MedicalRecordService;
import io.hospital.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

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
    }

    public void addDiagnosis() {
        System.out.println("Please enter the patient's first name:");
        String firstName = InputHandler.getStringInput();
        System.out.println("Please enter the patient's last name:");
        String lastName = InputHandler.getStringInput();
    }

    public void openNewRecord() {
        System.out.println("Please enter the patient's first name:");
        String firstName = InputHandler.getStringInput();
        System.out.println("Please enter the patient's last name:");
        String lastName = InputHandler.getStringInput();
        System.out.println("Please enter the patient notes:");
        String notes = InputHandler.getStringInput();

        medicalRecordService.openRecord(firstName, lastName, notes);
        System.out.println("Patient medical record has been opened.");
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
