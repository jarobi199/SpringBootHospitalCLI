package io.hospital.model;

import io.hospital.enums.Gender;
import io.hospital.enums.Status;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Patient {
    @Id
    private String id;
    private String wardId;
    private String doctorId;
    private LocalDate birthDate;
    private Gender gender;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private int contactNumber;
    private Status status;
    private LocalDateTime dischargeDate;
    private LocalDateTime admissionDate;

    public Patient() {
        //No argument constructor
    }

    public Patient(String wardId, String doctorId, LocalDate birthDate, Gender gender, String firstName, String lastName,
                   LocalDate dateOfBirth, int contactNumber, Status status, LocalDateTime dischargeDate, LocalDateTime admissionDate) {
        this.wardId = wardId;
        this.doctorId = doctorId;
        this.birthDate = birthDate;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.status = status;
        this.dischargeDate = dischargeDate;
        this.admissionDate = admissionDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(int contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(LocalDateTime dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public LocalDateTime getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDateTime admissionDate) {
        this.admissionDate = admissionDate;
    }
}
