package io.hospital.service;

import io.hospital.model.Patient;
import io.hospital.model.User;
import io.hospital.repository.PatientRepository;
import io.hospital.repository.UserRepository;
import io.hospital.util.CommandLineTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;

    public void listPatients(String username) {
        User doctor = userRepository.findByUsername(username).getFirst();
        if(doctor == null) {
            System.out.println("No doctor found for username: " + username);
        }
        else {
            System.out.println("DR. " + doctor.getName() + "'S PATIENT LIST:");
            List<Patient> patients = patientRepository.findByDoctorId(doctor.getId());
            CommandLineTable table = new CommandLineTable();
            table.setShowVerticalLines(true);
            table.setHeaders("FIRST NAME", "LAST NAME", "GENDER","DATE OF BIRTH","CONTACT NUMBER","STATUS","DISCHARGE DATE","ADMISSION DATE");
            for(Patient patient : patients) {
                table.addRow(patient.getFirstName(), patient.getLastName(), patient.getGender().name(), patient.getDateOfBirth().toString(),
                        String.valueOf(patient.getContactNumber()), patient.getDischargeDate().toString(), patient.getAdmissionDate().toString());
            }
            table.print();
        }


    }
}
