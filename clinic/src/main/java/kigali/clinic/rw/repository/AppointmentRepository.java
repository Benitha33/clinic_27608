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

    List<Appointment> findByDoctorIdAndAppointmentDateAndStatusNot(UUID doctorId, Date appointmentDate, AppointmentStatus status);

    List<Appointment> findByPatientIdAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(UUID patientId, Date date, List<AppointmentStatus> statuses);

    long countByStatus(AppointmentStatus status);

    long countByAppointmentDate(Date appointmentDate);

    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> countAppointmentsByStatus();

    @Modifying
    @Query("UPDATE Appointment a SET a.status = 'CANCELLED' WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date AND a.status <> 'COMPLETED'")
    int cancelAppointmentsForDoctorOnDate(@Param("doctorId") UUID doctorId, @Param("date") Date date);

    @Modifying
    @Query("DELETE FROM Appointment a WHERE a.status = 'CANCELLED' AND a.appointmentDate < :date")
    int deleteCancelledBefore(@Param("date") Date date);

    @Query("SELECT a.doctor.firstName, a.doctor.lastName, COUNT(a) FROM Appointment a WHERE a.status = 'COMPLETED' AND a.appointmentDate BETWEEN :start AND :end GROUP BY a.doctor.id, a.doctor.firstName, a.doctor.lastName ORDER BY COUNT(a) DESC")
    List<Object[]> findDoctorWorkload(@Param("start") Date start, @Param("end") Date end);
}
