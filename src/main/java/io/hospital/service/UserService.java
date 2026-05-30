package io.hospital.service;

import io.hospital.authentication.PasswordEncryptor;
import io.hospital.authentication.SessionContext;
import io.hospital.enums.Role;
import io.hospital.enums.Shift;
import io.hospital.enums.Specialization;
import io.hospital.factory.UserFactory;
import io.hospital.model.User;
import io.hospital.repository.UserRepository;
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

}
