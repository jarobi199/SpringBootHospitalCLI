package io.hospital.menu;

import io.hospital.enums.MenuAction;
import io.hospital.interfaces.IRoleMenu;
import io.hospital.util.InputHandler;

import java.util.List;

public class ReceptionistMenu implements IRoleMenu {

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
                                new MenuOption("Admit patient", MenuAction.ADMIT_PATIENT),
                                new MenuOption("Discharge patient", MenuAction.DISCHARGE_PATIENT),
                                new MenuOption("Assign to doctor", MenuAction.ASSIGN_TO_DOCTOR),
                                new MenuOption("Back", MenuAction.DELETE_WARD)
                        ));
                case 2 -> new WardMenu(
                        List.of(new MenuOption("List all wards", MenuAction.LIST_WARDS),
                                new MenuOption("View ward patients", MenuAction.VIEW_WARD_PATIENTS),
                                new MenuOption("Back", MenuAction.DELETE_WARD)
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
        System.out.println(" [1] Patients");
        System.out.println(" [2] Wards");
        System.out.println(" [0] Logout");
    }
}
