package io.hospital.menu;

import io.hospital.bridge.SpringContext;
import io.hospital.enums.MenuAction;
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

    }

    public void viewWardPatients() {
    }

    public void addWard() {
    }

    public void listWards() {
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
