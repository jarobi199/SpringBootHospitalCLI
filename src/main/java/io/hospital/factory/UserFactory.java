package io.hospital.factory;

import io.hospital.enums.Role;
import io.hospital.enums.Shift;
import io.hospital.enums.Specialization;
import io.hospital.model.Admin;
import io.hospital.model.Doctor;
import io.hospital.model.Receptionist;
import io.hospital.model.User;

public class UserFactory {

    public static User create(String fullName, String username, String password, Role role, int licenseNumber, Specialization specialization, Shift shift) {
        return switch (role) {
            case ADMINISTRATOR -> new Admin(fullName, username, password, role);
            case DOCTOR -> new Doctor(fullName, username, password, role, licenseNumber, specialization);
            case RECEPTIONIST ->  new Receptionist(fullName, username, password, role, shift);
        };
    }
}
