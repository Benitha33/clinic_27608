package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.PrescriptionRepository;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    public String savePrescription(UUID appointmentId, Prescription prescription) {
        Appointment appointment = appointmentRepo.findById(appointmentId).orElse(null);
        if (appointment == null) return "The appointment with that id does not exist";
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) return "Prescriptions are only for COMPLETED appointments";
        prescription.setIssuedDate(LocalDate.now());
        prescription.setAppointment(appointment);
        prescriptionRepo.save(prescription);
        return "Prescription saved successfully";
    }

    public List<Prescription> getByAppointment(UUID appointmentId) {
        return prescriptionRepo.findByAppointmentId(appointmentId);
    }

    public List<Prescription> getByPatient(String ssNumber) {
        return prescriptionRepo.findByPatientSsNumber(ssNumber);
    }

    public String updatePrescription(UUID id, Prescription updated) {
        Prescription existing = prescriptionRepo.findById(id).orElse(null);
        if (existing == null) return "The prescription with that id does not exist";
        existing.setMedicineName(updated.getMedicineName());
        existing.setDosage(updated.getDosage());
        existing.setDurationDays(updated.getDurationDays());
        prescriptionRepo.save(existing);
        return "Prescription updated successfully";
    }

    public String deletePrescription(UUID id) {
        if (!prescriptionRepo.existsById(id)) return "The prescription with that id does not exist";
        prescriptionRepo.deleteById(id);
        return "Prescription deleted successfully";
    }

    public List<Object[]> getTopMedicines() {
        return prescriptionRepo.findTopMedicines();
    }
}
