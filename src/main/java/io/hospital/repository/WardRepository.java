package io.hospital.repository;

import io.hospital.model.Ward;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends MongoRepository<Ward, String> {
    List<Ward> findByName(String name);
}