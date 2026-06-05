package io.hospital.menu;

import io.hospital.authentication.SessionContext;
import io.hospital.bridge.SpringContext;
import io.hospital.enums.MenuAction;
import io.hospital.interfaces.IRoleMenu;
import io.hospital.service.PatientService;
import io.hospital.util.InputHandler;

import java.util.List;

public class PatientMenu implements IRoleMenu {
    private final List<MenuOption> menuOptions;
    private final PatientService patientService;

    public PatientMenu(List<MenuOption> menuOptions) {
        this.menuOptions = menuOptions;
        this.patientService = SpringContext.getBean(PatientService.class);
    }

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            MenuAction menuAction = menuOptions.get(choice -1).action();
            switch (menuAction) {
                case LIST_PATIENTS -> listPatients();
                case LIST_MY_PATIENTS -> listMyPatients();
                case VIEW_PATIENT -> viewPatients();
                case ADMIT_PATIENT -> admitPatients();
                case DISCHARGE_PATIENT -> dischargePatient();
                case ASSIGN_TO_DOCTOR -> assignDoctor();
                case ASSIGN_TO_WARD -> assignWard();
            }
        }
        while (choice != 0);
    }

    public void assignWard() {
    }

    public void assignDoctor() {

    }

    public void dischargePatient() {

    }

    public void admitPatients() {

    }

    public void viewPatients() {

    }

    public void listMyPatients() {
       patientService.listPatients(SessionContext.getCurrentUser().getId());
    }

    public void listPatients() {
        patientService.listPatients();
    }

    @Override
    public void printOptions() {
        int i = 1;
        for(MenuOption menuOption : menuOptions) {
            System.out.println("[" + i + "] " + menuOption.label());
            i++;
        }
    }
}
