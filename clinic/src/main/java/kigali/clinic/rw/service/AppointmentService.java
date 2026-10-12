package kigali.clinic.rw.service;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public List<Appointment> getByStatus(AppointmentStatus status) {
        return appointmentRepo.findByStatusOrderByAppointmentDateAsc(status);
    }

    public List<Appointment> getBetweenDates(Date start, Date end) {
        return appointmentRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    public List<Object[]> getCountPerStatus() {
        return appointmentRepo.countAppointmentsByStatus();
    }

    @Transactional
    public String cancelAppointmentsForDay(UUID doctorId, Date date) {
        int count = appointmentRepo.cancelAppointmentsForDoctorOnDate(doctorId, date);
        return count + " appointments cancelled";
    }

    // Bonus +5: Pagination
    public Page<Appointment> getPage(Pageable pageable) {
        return appointmentRepo.findAll(pageable);
    }

    // Bonus +5: Delete cancelled before
    @Transactional
    public String deleteCancelledBefore(Date date) {
        int count = appointmentRepo.deleteCancelledBefore(date);
        return count + " appointments deleted";
    }
}
