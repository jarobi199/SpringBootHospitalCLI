package io.hospital.menu;

import io.hospital.interfaces.IRoleMenu;

public class GoodbyeMenu implements IRoleMenu {

    @Override
    public void show() {
        printOptions();
    }

    @Override
    public void printOptions() {
        System.out.println("Goodbye!");
    }
}
