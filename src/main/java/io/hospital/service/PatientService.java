package io.hospital.service;

import io.hospital.authentication.SessionContext;
import io.hospital.enums.Gender;
import io.hospital.enums.Status;
import io.hospital.model.Patient;
import io.hospital.model.User;
import io.hospital.model.Ward;
import io.hospital.repository.PatientRepository;
import io.hospital.repository.WardRepository;
import io.hospital.util.CommandLineTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private WardRepository wardRepository;

    public void listPatients(String doctorId) {
        System.out.println("DR. " + SessionContext.getCurrentUser().getName().toUpperCase() + "'S PATIENT LIST:");
        List<Patient> patients = patientRepository.findByDoctorId(doctorId);
        displayPatients(patients);
    }

    public void listPatients() {
        List<Patient> patients = patientRepository.findAll();
        displayPatients(patients);
    }

    private void displayPatients(List<Patient> patients) {
        CommandLineTable table = new CommandLineTable();
        table.setShowVerticalLines(true);
        table.setHeaders("FIRST NAME", "LAST NAME", "GENDER", "DATE OF BIRTH", "CONTACT NUMBER", "STATUS", "DISCHARGE DATE", "ADMISSION DATE");
        for (Patient patient : patients) {
            table.addRow(patient.getFirstName(), patient.getLastName(), patient.getGender().name(), patient.getDateOfBirth().toString(),
                    String.valueOf(patient.getContactNumber()), patient.getDischargeDate().toString(), patient.getAdmissionDate().toString());
        }
        table.print();
    }

    public void admitPatient(String firstName, String lastName, Gender gender, int phoneNumber,
                             LocalDate birthDate, LocalDateTime admissionDate, Status status, User doctor, Ward ward) {
        Patient patient = new Patient(ward.getId(), doctor.getId(), birthDate, gender, firstName, lastName, birthDate, phoneNumber, status, null, admissionDate);
        patientRepository.save(patient);

        ward.setCurrentOccupancy(ward.getCurrentOccupancy() + 1);
        wardRepository.save(ward);

        //TODO: Execute WardCapacityStrategy
    }
}
