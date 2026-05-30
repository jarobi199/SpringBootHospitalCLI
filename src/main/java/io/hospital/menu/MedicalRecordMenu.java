package io.hospital.menu;

import io.hospital.interfaces.IRoleMenu;
import io.hospital.util.InputHandler;

public class MedicalRecordMenu implements IRoleMenu {
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
    }

    public void openNewRecord() {
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
