package kigali.clinic.rw.service;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.repository.AppointmentRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    public String saveAppointment(Appointment appointment) {
        // A4: No double booking check
        boolean isBooked = appointmentRepo.existsByDoctorIdAndAppointmentDateAndStatusNot(
                appointment.getDoctor().getId(),
                appointment.getAppointmentDate(),
                AppointmentStatus.CANCELLED
        );
        if (isBooked) {
            return "Doctor is already booked on that date";
        }
        appointmentRepo.save(appointment);
        return "Appointment saved successfully";
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    // A2
    public List<Appointment> getByStatus(AppointmentStatus status) {
        return appointmentRepo.findByStatusOrderByAppointmentDateAsc(status);
    }

    // A3
    public List<Appointment> getBetweenDates(Date start, Date end) {
        return appointmentRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    // C1
    public List<Object[]> getCountPerStatus() {
        return appointmentRepo.countAppointmentsByStatus();
    }

    // C3
    public List<Object[]> getBusiestOffice() {
        return appointmentRepo.findBusiestOffice();
    }

    // C4 (Bonus)
    @Transactional
    public String cancelAppointmentsForDay(UUID doctorId, Date date) {
        int count = appointmentRepo.cancelAppointmentsForDoctorOnDate(doctorId, date);
        return count + " appointments cancelled";
    }

    // Slide 4 #4
    public List<Appointment> getByDoctorSpecialization(String specName) {
        return appointmentRepo.findAppointmentsByDoctorSpecialization(specName);
    }
}