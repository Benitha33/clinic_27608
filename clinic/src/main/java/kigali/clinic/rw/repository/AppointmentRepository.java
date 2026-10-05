package kigali.clinic.rw.repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status);

    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(Date start, Date end);

    boolean existsByDoctorIdAndAppointmentDateAndStatusNot(UUID doctorId, Date appointmentDate, AppointmentStatus status);

    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> countAppointmentsByStatus();

    @Query("SELECT o.name, o.officeNumber, COUNT(a) FROM Appointment a JOIN a.doctor d JOIN d.office o GROUP BY o.id, o.name, o.officeNumber ORDER BY COUNT(a) DESC")
    List<Object[]> findBusiestOffice();

    @Modifying
    @Query("UPDATE Appointment a SET a.status = 'CANCELLED' WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date AND a.status <> 'COMPLETED'")
    int cancelAppointmentsForDoctorOnDate(@Param("doctorId") UUID doctorId, @Param("date") Date date);

    @Query("SELECT a FROM Appointment a JOIN a.doctor d JOIN d.specializations s WHERE LOWER(s.name) = LOWER(:specName)")
    List<Appointment> findAppointmentsByDoctorSpecialization(@Param("specName") String specName);
}