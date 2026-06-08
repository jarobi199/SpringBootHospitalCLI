package io.hospital.service;

import io.hospital.enums.WardType;
import io.hospital.model.Patient;
import io.hospital.model.User;
import io.hospital.model.Ward;
import io.hospital.repository.PatientRepository;
import io.hospital.repository.UserRepository;
import io.hospital.repository.WardRepository;
import io.hospital.util.CommandLineTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WardService {

    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;

    public void deleteWard(String wardName) {
        List<Ward> wards = wardRepository.findByName(wardName);
        wardRepository.deleteAll(wards);
        System.out.println("Ward has been deleted!");
    }

    public void listWardPatients(String wardName) {
        List<Ward> wards = wardRepository.findByName(wardName);
        for(Ward ward : wards) {
            System.out.println("WARD: " + ward.getName());
            List<Patient> patients = patientRepository.findByWardId(ward.getId());
            CommandLineTable table = new CommandLineTable();
            table.setShowVerticalLines(true);
            table.setHeaders("FIRST NAME", "LAST NAME", "DOCTOR", "GENDER", "DATE OF BIRTH", "CONTACT NUMBER", "STATUS", "ADMISSION DATE", "DISCHARGE DATE" );
            for (Patient patient : patients) {
                Optional<User> doctor = userRepository.findById(patient.getDoctorId());
                table.addRow(patient.getFirstName(), patient.getLastName(), (doctor.isEmpty()) ? "N/A" : doctor.get().getName(), patient.getGender().name(), patient.getDateOfBirth().toString(),
                        String.valueOf(patient.getContactNumber()), patient.getStatus().name(), patient.getAdmissionDate().toString(), (patient.getDischargeDate() == null) ? "N/A" : patient.getDischargeDate().toString());
            }
            table.print();
        }
    }

    public void addWard(String wardName, WardType wardType, int totalBeds) {
        Ward ward = new Ward(wardName, wardType, totalBeds);
        wardRepository.save(ward);
    }

    public void listWards() {
        List<Ward> wards = wardRepository.findAll();
        CommandLineTable table = new CommandLineTable();
        table.setHeaders("NAME", "WARD TYPE", "TOTAL BEDS","CURRENT OCCUPANCY","OCCUPANCY PERCENTAGE");
        table.setShowVerticalLines(true);
        wards.forEach(ward -> {
            int occupancyPercentage = ward.getOccupancyPercent();
            int filled = Math.round(occupancyPercentage / 10.0f);
            String alert =occupancyPercentage >= 80 ? "This ward is over 80% capacity! ⚠" : "";
            String bar = "▓".repeat(filled) + "░".repeat(10 - filled) + " " + alert;
            table.addRow(ward.getName(), ward.getWardType().name(), String.valueOf(ward.getTotalBeds()), String.valueOf(ward.getCurrentOccupancy()), bar);
        });
        table.print();
    }

    public List<Ward> getWards() {
        return wardRepository.findAll();
    }

}
