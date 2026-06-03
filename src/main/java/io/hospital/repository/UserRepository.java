package io.hospital.repository;

import io.hospital.enums.Role;
import io.hospital.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByUsername(String name);
    List<User> findByRole(Role role);
}
