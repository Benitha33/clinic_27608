package kigali.clinic.rw.controller;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // A4: Save with double-booking check
    @PostMapping("/save")
    public ResponseEntity<String> saveAppointment(@RequestBody Appointment appointment) {
        String result = appointmentService.saveAppointment(appointment);
        if (result.contains("already booked")) {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    // A2
    @GetMapping("/by-status")
    public List<Appointment> getByStatus(@RequestParam AppointmentStatus status) {
        return appointmentService.getByStatus(status);
    }

    // A3
    @GetMapping("/between")
    public List<Appointment> getBetweenDates(@RequestParam String start, @RequestParam String end) {
        return appointmentService.getBetweenDates(Date.valueOf(start), Date.valueOf(end));
    }

    // C1
    @GetMapping("/status/by-status")
    public List<Object[]> getCountPerStatus() {
        return appointmentService.getCountPerStatus();
    }

    // C3
    @GetMapping("/busiest-office")
    public List<Object[]> getBusiestOffice() {
        return appointmentService.getBusiestOffice();
    }

    // C4 (Bonus)
    @PatchMapping("/cancel-day")
    public String cancelDay(@RequestParam UUID doctorId, @RequestParam String date) {
        return appointmentService.cancelAppointmentsForDay(doctorId, Date.valueOf(date));
    }

    // Slide 4 #4
    @GetMapping("/by-specialization")
    public List<Appointment> getBySpecialization(@RequestParam String specName) {
        return appointmentService.getByDoctorSpecialization(specName);
    }
}