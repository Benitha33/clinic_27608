package kigali.clinic.rw.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private SpecializationRepository specializationRepo;

    public String saveDoctor(Doctor doctor) {
        doctorRepo.save(doctor);
        return "Doctor saved successfully";
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }

    public Doctor getDoctorById(UUID id) {
        return doctorRepo.findById(id).orElse(null);
    }

    public List<Doctor> getDoctorsBySpecialization(String name) {
        return doctorRepo.findDoctorsBySpecializationName(name);
    }

    public List<Doctor> getDoctorsWithoutOffice() {
        return doctorRepo.findDoctorsWithoutOffice();
    }

    public List<Object[]> getAppointmentCountPerDoctor() {
        return doctorRepo.countAppointmentsPerDoctor();
    }

    public String addSpecialization(UUID doctorId, UUID specializationId) {
        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);
        if (doctor == null) return "The doctor with that id does not exist";
        Specialization spec = specializationRepo.findById(specializationId).orElse(null);
        if (spec == null) return "That specialization does not exist";
        if (doctor.getSpecializations().contains(spec)) return "The doctor already has that specialization";
        doctor.getSpecializations().add(spec);
        doctorRepo.save(doctor);
        return "Specialization added to the doctor";
    }

    public String removeSpecialization(UUID doctorId, UUID specializationId) {
        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);
        if (doctor == null) return "The doctor with that id does not exist";
        Specialization spec = specializationRepo.findById(specializationId).orElse(null);
        if (spec == null) return "That specialization does not exist";
        if (!doctor.getSpecializations().contains(spec)) return "The doctor does not have that specialization";
        doctor.getSpecializations().remove(spec);
        doctorRepo.save(doctor);
        return "Specialization removed from the doctor";
    }
}
