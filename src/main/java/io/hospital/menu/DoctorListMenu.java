package io.hospital.menu;

import io.hospital.bridge.SpringContext;
import io.hospital.interfaces.IRoleMenu;
import io.hospital.service.PatientService;
import io.hospital.service.UserService;
import io.hospital.util.InputHandler;

public class DoctorListMenu implements IRoleMenu {

    private final UserService userService;
    private final PatientService patientService;

    public DoctorListMenu() {
        this.userService = SpringContext.getBean(UserService.class);
        this.patientService = SpringContext.getBean(PatientService.class);
    }

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> listAllDoctors();
                case 2 -> viewDoctorPatients();
                case 3 -> setAvailability();
            }
        }
        while (choice != 0);
    }

    public void listAllDoctors() {

        userService.listAllDoctors();
    }

    public void viewDoctorPatients() {
        System.out.println("Please enter the username of the doctor:");
        String username = InputHandler.getStringInput();
        patientService.listPatients(username);
    }

    public void setAvailability() {
        System.out.println("Please enter the username of the doctor you want to change availability for:");
        String username = InputHandler.getStringInput();
        userService.changeAvailability(username);
    }


    @Override
    public void printOptions() {
        System.out.println("[1] List all doctors");
        System.out.println("[2] View doctor patients");
        System.out.println("[3] Set availability");
        System.out.println("[0] Back");
    }
}
