package io.hospital.model;

import io.hospital.enums.Role;
import io.hospital.enums.Shift;
import io.hospital.interfaces.IRoleMenu;
import io.hospital.menu.ReceptionistMenu;

public class Receptionist extends User {

    private Shift shift;

    public Receptionist() {
        //No argument constructor
    }

    public Receptionist(String name, String username, String password, Role role, Shift shift) {
        super(name, username, password, role);
        this.shift = shift;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    @Override
    public IRoleMenu getMenu() {
        return new ReceptionistMenu();
    }
}
