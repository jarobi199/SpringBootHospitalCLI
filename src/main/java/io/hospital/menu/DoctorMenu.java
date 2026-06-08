package io.hospital.menu;

import io.hospital.enums.MenuAction;
import io.hospital.interfaces.IRoleMenu;
import io.hospital.util.InputHandler;

import java.util.List;

public class DoctorMenu implements IRoleMenu {
    @Override
    public void show() {
        int choice;
        IRoleMenu menu;

        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            menu = switch (choice) {
                case 1 -> new PatientMenu(
                        List.of(new MenuOption("List my patients", MenuAction.LIST_MY_PATIENTS),
                                new MenuOption("View patient detail", MenuAction.VIEW_PATIENT),
                                new MenuOption("Assign to doctor", MenuAction.ASSIGN_TO_DOCTOR)
                        ));
                case 2 -> new MedicalRecordMenu();
                case 3 -> new WardMenu(
                        List.of(new MenuOption("List all wards", MenuAction.LIST_WARDS),
                                new MenuOption("View ward patients", MenuAction.VIEW_WARD_PATIENTS)
                        ));
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
        System.out.println("[1] Patients");
        System.out.println("[2] Medical Records");
        System.out.println("[3] Wards");
        System.out.println("[0] Back");
    }
}
