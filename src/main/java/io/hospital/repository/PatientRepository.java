package io.hospital.repository;

import io.hospital.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
    List<Patient> findByDoctorId(String doctorId);
    List<Patient> findByWardId(String wardId);
    List<Patient> findByAdmissionDateIsNotNullAndDischargeDateIsNull();
    List<Patient> findByAdmissionDateIsNotNullAndDischargeDateIsNotNull();
}
