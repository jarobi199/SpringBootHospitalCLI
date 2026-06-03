package io.hospital.menu;

import io.hospital.enums.MenuAction;
import io.hospital.interfaces.IRoleMenu;
import io.hospital.util.InputHandler;

import java.util.List;

public class AdminMenu implements IRoleMenu {

    @Override
    public void show() {
        int choice;
        IRoleMenu menu;

        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            menu = switch (choice) {
                case 1 -> new PatientMenu(
                        List.of(new MenuOption("List all patients", MenuAction.LIST_PATIENTS),
                                new MenuOption("View patient detail", MenuAction.VIEW_PATIENT),
                                new MenuOption("Admit patient", MenuAction.ADMIT_PATIENT),
                                new MenuOption("Discharge patient", MenuAction.DISCHARGE_PATIENT),
                                new MenuOption("Assign to doctor", MenuAction.ASSIGN_TO_DOCTOR),
                                new MenuOption("Assign to ward", MenuAction.ASSIGN_TO_WARD),
                                new MenuOption("Back", MenuAction.DELETE_WARD)
                        ));
                case 2 -> new WardMenu(
                        List.of(new MenuOption("List all wards", MenuAction.LIST_WARDS),
                                new MenuOption("Add ward", MenuAction.ADD_WARD),
                                new MenuOption("View ward patients", MenuAction.VIEW_WARD_PATIENTS),
                                new MenuOption("Delete ward", MenuAction.DELETE_WARD),
                                new MenuOption("Back", MenuAction.DELETE_WARD)
                        ));
                case 3 -> new DoctorListMenu();
                case 4 -> new MedicalRecordMenu();
                case 5 -> new ReportMenu();
                case 6 -> new StaffAccountMenu();
                case 0 -> new GoodbyeMenu();
                default -> throw new IllegalStateException("Unexpected value: " + choice);
            };
            menu.show();
        }
        while (choice != 0);

        InputHandler.closeInput();
    }

    @Override
    public void printOptions() {
        System.out.println(" [1] Patients");
        System.out.println(" [2] Wards");
        System.out.println(" [3] Doctors");
        System.out.println(" [4] Medical records");
        System.out.println(" [5] Reports");
        System.out.println(" [6] Staff accounts");
        System.out.println(" [0] Logout");
    }
}
