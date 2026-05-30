package io.hospital.model;

import io.hospital.enums.Role;
import io.hospital.enums.Specialization;
import io.hospital.interfaces.IRoleMenu;
import io.hospital.menu.DoctorMenu;

public class Doctor extends User {

    private int licenseNumber;
    private Specialization specialization;
    private boolean isAvailable;

    public Doctor(String name, String username, String password, Role role, int licenseNumber, Specialization specialization) {
        super(name, username, password, role);
        this.licenseNumber = licenseNumber;
        this.specialization = specialization;
    }

    public int getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(int licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public IRoleMenu getMenu() {
        return new DoctorMenu();
    }

}
