package io.hospital.service;

import io.hospital.model.MedicalRecord;
import io.hospital.model.Patient;
import io.hospital.repository.MedicalRecordRepository;
import io.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private PatientRepository patientRepository;


    public void openRecord(String firstName, String lastName, String notes) {
        Patient patient = patientRepository.findByFirstNameAndLastName(firstName, lastName).getFirst();
        MedicalRecord medicalRecord = new MedicalRecord(patient.getId(), patient.getDoctorId(), LocalDateTime.now(), notes);
        medicalRecordRepository.save(medicalRecord);
    }
}
