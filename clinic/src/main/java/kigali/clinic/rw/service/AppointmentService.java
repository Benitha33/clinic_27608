package kigali.clinic.rw.service;

import java.sql.Date;
import java.time.LocalDate;
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
        Date today = Date.valueOf(LocalDate.now());
        if (appointment.getAppointmentDate().before(today)) {
            return "Appointment date cannot be in the past";
        }
        boolean isBooked = appointmentRepo.existsByDoctorIdAndAppointmentDateAndStatusNot(
                appointment.getDoctor().getId(),
                appointment.getAppointmentDate(),
                AppointmentStatus.CANCELLED
        );
        if (isBooked) {
            return "Doctor is already booked on that date";
        }
        appointment.setStatus(AppointmentStatus.SCHEDULED);
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

    public Page<Appointment> getPage(Pageable pageable) {
        return appointmentRepo.findAll(pageable);
    }

    @Transactional
    public String deleteCancelledBefore(Date date) {
        int count = appointmentRepo.deleteCancelledBefore(date);
        return count + " appointments deleted";
    }

    public String changeStatus(UUID id, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepo.findById(id).orElse(null);
        if (appointment == null) return "The appointment with that id does not exist";
        AppointmentStatus current = appointment.getStatus();
        boolean allowed = false;
        if (current == AppointmentStatus.SCHEDULED && (newStatus == AppointmentStatus.CONFIRMED || newStatus == AppointmentStatus.CANCELLED)) allowed = true;
        if (current == AppointmentStatus.CONFIRMED && (newStatus == AppointmentStatus.COMPLETED || newStatus == AppointmentStatus.CANCELLED)) allowed = true;
        if (!allowed) return "Cannot change status from " + current + " to " + newStatus;
        appointment.setStatus(newStatus);
        appointmentRepo.save(appointment);
        return "Appointment status updated successfully";
    }

    public String reschedule(UUID id, Date newDate) {
        Appointment appointment = appointmentRepo.findById(id).orElse(null);
        if (appointment == null) return "The appointment with that id does not exist";
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) return "A COMPLETED appointment cannot be rescheduled";
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) return "A CANCELLED appointment cannot be rescheduled";
        Date today = Date.valueOf(LocalDate.now());
        if (newDate.before(today)) return "Appointment date cannot be in the past";
        boolean isBooked = appointmentRepo.existsByDoctorIdAndAppointmentDateAndStatusNot(
                appointment.getDoctor().getId(),
                newDate,
                AppointmentStatus.CANCELLED
        );
        if (isBooked) return "Doctor is already booked on that date";
        appointment.setAppointmentDate(newDate);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepo.save(appointment);
        return "Appointment rescheduled successfully";
    }

    public boolean existsById(UUID id) {
        return appointmentRepo.existsById(id);
    }

    public void deleteById(UUID id) {
        appointmentRepo.deleteById(id);
    }
}
