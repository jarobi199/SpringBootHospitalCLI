package io.hospital.menu;

import io.hospital.bridge.SpringContext;
import io.hospital.enums.MenuAction;
import io.hospital.enums.WardType;
import io.hospital.interfaces.IRoleMenu;
import io.hospital.service.WardService;
import io.hospital.util.InputHandler;

import java.util.List;

public class WardMenu implements IRoleMenu {

    private final List<MenuOption> menuOptions;
    private final WardService wardService;

    public WardMenu(List<MenuOption> menuOptions) {
        this.menuOptions = menuOptions;
        this.wardService = SpringContext.getBean(WardService.class);
    }

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            MenuAction menuAction = menuOptions.get(choice -1).action();
            switch (menuAction) {
                case LIST_WARDS -> listWards();
                case ADD_WARD -> addWard();
                case VIEW_WARD_PATIENTS -> viewWardPatients();
                case DELETE_WARD -> deleteWard();
            }
        }
        while (choice != 0);
    }

    public void deleteWard() {
        System.out.println("Enter the name of the ward that you want to delete:");
        String wardName = InputHandler.getStringInput();
        wardService.deleteWard(wardName);
    }

    public void viewWardPatients () {
        System.out.println("Enter the name of the ward that you want to view the patients:");
        String wardName = InputHandler.getStringInput();
        wardService.listWardPatients(wardName);
    }

    public void addWard() {
        System.out.println("Enter the name of the ward");
        String wardName = InputHandler.getStringInput();
        System.out.println("Enter the ward type (GENERAL, ICU, PAEDIATRIC, MATERNITY, SURGICAL, ONCOLOGY):");
        WardType wardType = WardType.valueOf(InputHandler.getStringInput());
        System.out.println("Enter the number of beds:");
        int totalBeds = InputHandler.getIntegerInput();

        wardService.addWard(wardName, wardType, totalBeds);
        System.out.println("Ward added!");
    }

    public void listWards() {
        wardService.listWards();
    }

    private WardType wardType;
    private int totalBeds;
    private int currentOccupancy;

    @Override
    public void printOptions() {
        int i = 1;
        for(MenuOption menuOption : menuOptions) {
            System.out.println("[" + i + "] " + menuOption.label());
            i++;
        }
    }
}
