package io.hospital.menu;

import io.hospital.authentication.SessionContext;
import io.hospital.bridge.SpringContext;
import io.hospital.enums.Gender;
import io.hospital.enums.MenuAction;
import io.hospital.enums.Status;
import io.hospital.interfaces.IRoleMenu;
import io.hospital.model.Doctor;
import io.hospital.model.Patient;
import io.hospital.model.User;
import io.hospital.model.Ward;
import io.hospital.service.PatientService;
import io.hospital.service.UserService;
import io.hospital.service.WardService;
import io.hospital.util.InputHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PatientMenu implements IRoleMenu {
    private final List<MenuOption> menuOptions;
    private final PatientService patientService;
    private final WardService wardService;
    private final UserService userService;

    public PatientMenu(List<MenuOption> menuOptions) {
        this.menuOptions = menuOptions;
        this.patientService = SpringContext.getBean(PatientService.class);
        this.wardService = SpringContext.getBean(WardService.class);
        this.userService = SpringContext.getBean(UserService.class);
    }

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            if (choice != 0) {
                MenuAction menuAction = menuOptions.get(choice -1).action();
                switch (menuAction) {
                    case LIST_PATIENTS -> listPatients();
                    case LIST_MY_PATIENTS -> listMyPatients();
                    case VIEW_PATIENT -> viewPatient();
                    case ADMIT_PATIENT -> admitPatients();
                    case DISCHARGE_PATIENT -> dischargePatient();
                    case ASSIGN_TO_DOCTOR -> assignDoctor();
                }
            }
        }
        while (choice != 0);
    }

    public void assignDoctor() {
        Patient patient = listPatientsAndSelect();
        User doctor = listDoctorsAndSelect();
        patientService.assignDoctor(patient, doctor);
    }

    public void dischargePatient() {
        Patient patient = listPatientsAndSelect();
        patientService.dischargePatient(patient);
    }

    public void admitPatients() {
        System.out.println("Please enter the patient's first name:");
        String firstName = InputHandler.getStringInput();
        System.out.println("Please enter the patient's last name:");
        String lastName = InputHandler.getStringInput();
        System.out.println("Please enter your gender (MALE, FEMALE):");
        Gender gender = Gender.valueOf(InputHandler.getStringInput());
        System.out.println("Please enter your phone number:");
        int phoneNumber = InputHandler.getIntegerInput();
        System.out.println("Please enter your date of birth(yyyy-MM-dd):");
        LocalDate birthDate = LocalDate.parse(InputHandler.getStringInput());
        Status status = Status.ADMITTED;
        LocalDateTime admissionDate = LocalDateTime.now();
        User doctor = listDoctorsAndSelect();
        Ward ward = listWardsAndSelect();

        patientService.admitPatient(firstName, lastName, gender, phoneNumber, birthDate, admissionDate, status, doctor,ward);
    }

    private User listDoctorsAndSelect() {
        int i = 1;
        List<Doctor> availableDoctors = userService.findAllAvailableDoctors();
        for (Doctor doctor : availableDoctors) {
            System.out.println(i + ") " + doctor.getName() + " (" + doctor.getSpecialization() + ")");
            i++;
        }
        System.out.println("Please select a doctor:");
        int doctorIndex = InputHandler.getIntegerInput() - 1;
        return availableDoctors.get(doctorIndex);
    }

    private Ward listWardsAndSelect() {
        int i = 1;
        List<Ward> allWards = wardService.getWards();
        for (Ward ward : allWards) {
            System.out.println(i + ") " + ward.getName());
            i++;
        }
        System.out.println("Please select a ward:");
        int wardIndex = InputHandler.getIntegerInput() - 1;
        return allWards.get(wardIndex);
    }

    private Patient listPatientsAndSelect() {
        int i = 1;
        List<Patient> allPatients = patientService.getPatients();
        for (Patient patient : allPatients) {
            System.out.println(i + ") " + patient.getFirstName() + " " + patient.getLastName());
        }
        System.out.println("Please select a patient:");
        int patientIndex = InputHandler.getIntegerInput() - 1;
        return allPatients.get(patientIndex);
    }

    public void viewPatient() {

    }

    public void listMyPatients() {
       patientService.listPatients(SessionContext.getCurrentUser().getId());
    }

    public void listPatients() {
        patientService.listPatients();
    }

    @Override
    public void printOptions() {
        int i = 1;
        for(MenuOption menuOption : menuOptions) {
            System.out.println("[" + i + "] " + menuOption.label());
            i++;
        }
        System.out.println("[0] Back");
    }
}
