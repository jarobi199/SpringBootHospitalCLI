package io.hospital.menu;

import io.hospital.bridge.SpringContext;
import io.hospital.enums.Role;
import io.hospital.enums.Shift;
import io.hospital.enums.Specialization;
import io.hospital.interfaces.IRoleMenu;
import io.hospital.service.UserService;
import io.hospital.util.InputHandler;

public class StaffAccountMenu implements IRoleMenu {

    private final UserService userService = SpringContext.getBean(UserService.class);

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> listStaff();
                case 2 -> addStaffMember();
                case 3 -> deleteStaffMember();
                case 4 -> changePassword();
            }
        }
        while (choice != 0);
    }

    public void changePassword() {
        System.out.print("Enter new password: ");
        String newPassword = InputHandler.getStringInput();
        if (userService.changePassword(newPassword)) {
            System.out.println("Password changed successfully!");
        }
        else
        {
            System.out.println("Password not changed. Old PIN and new PIN cannot be the same!");
        }
    }

    public void deleteStaffMember() {
        System.out.print("Enter username of the user to be deleted: ");
        String username = InputHandler.getStringInput();
        if (userService.deleteUser(username)) {
            System.out.println("User deleted successfully!");
        }
        else
        {
            System.out.println("User not found!");
        }
    }

    public void addStaffMember() {
        System.out.println("Please enter your full name:");
        String fullName = InputHandler.getStringInput();
        System.out.println("Please enter your username:");
        String username = InputHandler.getStringInput();
        System.out.println("Please enter your password:");
        String password = InputHandler.getStringInput();
        System.out.println("Please enter your role (ADMIN, RECEPTIONIST, DOCTOR):");
        Role role = Role.valueOf(InputHandler.getStringInput().toUpperCase());
        int licenseNumber = 0;
        Specialization specialization = null;
        Shift shift = null;

        if(Role.DOCTOR.equals(role)) {
            System.out.println("Please enter your license number:");
            licenseNumber = InputHandler.getIntegerInput();
            System.out.println("Please enter your specialization (GENERAL_PRACTICE, CARDIOLOGY, NEUROLOGY, ONCOLOGY, PAEDIATRICS , SURGERY:");
            specialization = Specialization.valueOf(InputHandler.getStringInput().toUpperCase());
        }
        else if(Role.RECEPTIONIST.equals(role)) {
            System.out.println("Please enter your shift (MORNING, EVENING, NIGHT):");
            shift = Shift.valueOf(InputHandler.getStringInput());
        }

        userService.addUser(fullName, username, password, role, licenseNumber, specialization, shift);
        System.out.println("User added successfully!");
    }

    public void listStaff() {
        userService.listStaff();
    }

    @Override
    public void printOptions() {
        System.out.println("[1] List staff");
        System.out.println("[2] Add staff member");
        System.out.println("[3] Delete staff member");
        System.out.println("[4] Change password");
        System.out.println("[0] Back");
    }

}


