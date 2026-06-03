package io.hospital.service;

import io.hospital.authentication.PasswordEncryptor;
import io.hospital.authentication.SessionContext;
import io.hospital.enums.Role;
import io.hospital.enums.Shift;
import io.hospital.enums.Specialization;
import io.hospital.factory.UserFactory;
import io.hospital.model.Doctor;
import io.hospital.model.User;
import io.hospital.repository.UserRepository;
import io.hospital.util.CommandLineTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public boolean authenticate(String username, String password) {
        boolean authenticated = false;
        User user = findByUser(username);
        if ((user != null) && PasswordEncryptor.authenticate(password, user.getPassword())) {
            authenticated = true;
            SessionContext.setCurrentUser(user);
        }

        return authenticated;
    }

   public void addUser(String fullName, String username, String password, Role role, int licenseNumber, Specialization specialization, Shift shift) {
        String encrypted = PasswordEncryptor.encrypt(password);
        User user = UserFactory.create(fullName, username, encrypted, role, licenseNumber, specialization, shift);
        userRepository.save(user);
    }

    public User findByUser(String username) {
        List<User> users = userRepository.findByUsername(username);
        return (users.isEmpty()) ? null : users.getFirst();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public boolean changePassword(String newPassword) {
        boolean changed = false;
        String oldPassword = SessionContext.getCurrentUser().getPassword();
        if (!oldPassword.equals(newPassword)) {
            SessionContext.getCurrentUser().setPassword(PasswordEncryptor.encrypt(newPassword));
            userRepository.save(SessionContext.getCurrentUser());
            changed = true;
        }

        return changed;
    }

    public boolean deleteUser(String username) {
        boolean deleted = false;
        User user = findByUser(username);
        if (user != null) {
            userRepository.delete(user);
            deleted = true;
        }
        return deleted;
    }

    public void listStaff() {
        CommandLineTable table = new CommandLineTable();
        table.setShowVerticalLines(true);
        table.setHeaders("FULL NAME", "USERNAME", "PASSWORD", "ROLE");
        for(User user : userRepository.findAll() ) {
            table.addRow(user.getName(), user.getUsername(), user.getPassword(),user.getRole().name());
        }
        table.print();
    }

    public void listAllDoctors() {
        CommandLineTable table = new CommandLineTable();
        table.setShowVerticalLines(true);
        table.setHeaders("FULL NAME", "USERNAME", "PASSWORD", "ROLE","LICENSE NUMBER","SPECIALIZATION","AVAILABLE");
        for(User user : userRepository.findByRole(Role.DOCTOR) ) {
            Doctor doctor = (Doctor) user;
            table.addRow(doctor.getName(), doctor.getUsername(), doctor.getPassword(),doctor.getRole().name(),
                    String.valueOf(doctor.getLicenseNumber()), doctor.getSpecialization().name(), String.valueOf(doctor.isAvailable()));
        }
        table.print();
    }

    public void changeAvailability(String username) {
        Doctor doctor = (Doctor) findByUser(username);
        doctor.setAvailable(!doctor.isAvailable());
        System.out.println("The doctor's current availability has been changed to: " + (doctor.isAvailable() ? "AVAILABLE" : "UNAVAILABLE"));
    }


}
