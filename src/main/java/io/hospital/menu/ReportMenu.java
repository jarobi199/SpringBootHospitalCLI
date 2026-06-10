package io.hospital.menu;

import io.hospital.interfaces.IRoleMenu;
import io.hospital.service.ReportService;
import io.hospital.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class ReportMenu implements IRoleMenu {

    @Autowired
    private ReportService reportService;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> hospitalSummary();
                case 2 -> wardOccupancy();
                case 3 -> activePrescriptions();
                case 4 -> longStayPatients();
            }
        }
        while (choice != 0);
    }

    public void longStayPatients() {
    }

    public void activePrescriptions() {
    }

    public void wardOccupancy() {
    }

    public void hospitalSummary() {
        reportService.hospitalSummary();
    }


    @Override
    public void printOptions() {
        System.out.println("[1] Hospital summary");
        System.out.println("[2] Ward occupancy");
        System.out.println("[3] Active prescriptions");
        System.out.println("[4] Long stay patients");
        System.out.println("[0] Back");
    }
}
