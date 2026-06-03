package io.hospital.service;

import io.hospital.model.Patient;
import io.hospital.model.Ward;
import io.hospital.repository.PatientRepository;
import io.hospital.repository.WardRepository;
import io.hospital.util.CommandLineTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WardService {

    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private PatientRepository patientRepository;

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
            table.setHeaders("FIRST NAME", "LAST NAME", "GENDER","DATE OF BIRTH","CONTACT NUMBER","STATUS","DISCHARGE DATE","ADMISSION DATE");
            for(Patient patient : patients) {
                    table.addRow(patient.getFirstName(), patient.getLastName(), patient.getGender().name(), patient.getDateOfBirth().toString(),
                            String.valueOf(patient.getContactNumber()), patient.getDischargeDate().toString(), patient.getAdmissionDate().toString());
            }
            table.print();
        }
    }
}
