package kigali.clinic.rw.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    public String savePatient(Patient patient) {
        patientRepo.save(patient);
        return "Patient saved successfully";
    }

    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    public List<Patient> getByLastName(String lastName) {
        return patientRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public List<Patient> getPatientsOfDoctor(UUID doctorId) {
        return patientRepo.findPatientsByDoctorId(doctorId);
    }

    public List<Patient> getFrequentPatients(long min) {
        return patientRepo.findFrequentPatients(min);
    }

    public boolean existsBySsNumber(String ssNumber) {
        return patientRepo.existsBySsNumber(ssNumber);
    }

    public List<Appointment> getUpcoming(String ssNumber) {
        Patient patient = patientRepo.findBySsNumber(ssNumber);
        if (patient == null) return null;
        return appointmentRepo.findByPatientIdAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(
                patient.getId(),
                Date.valueOf(LocalDate.now()),
                List.of(AppointmentStatus.SCHEDULED, AppointmentStatus.CONFIRMED)
        );
    }
}
