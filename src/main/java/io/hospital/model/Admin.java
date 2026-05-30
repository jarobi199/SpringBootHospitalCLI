package io.hospital.model;

import io.hospital.enums.Role;
import io.hospital.interfaces.IRoleMenu;
import io.hospital.menu.AdminMenu;

public class Admin extends User {

    public Admin(String name, String username, String password, Role role) {
        super(name, username, password, role);
    }

    @Override
    public IRoleMenu getMenu() {
        return new AdminMenu();
    }
}
