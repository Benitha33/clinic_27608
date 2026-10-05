package kigali.clinic.rw;

import java.sql.Date;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.OfficeRepository;
import kigali.clinic.rw.repository.PatientRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@SpringBootApplication
public class ClinicApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinicApplication.class, args);
    }

    @Bean
    CommandLineRunner seedData(
            SpecializationRepository specRepo,
            OfficeRepository officeRepo,
            DoctorRepository doctorRepo,
            PatientRepository patientRepo,
            AppointmentRepository apptRepo) {
        return args -> {
            if (specRepo.count() > 0) {
                System.out.println("✅ Data already present. Skipping seed.");
                return;
            }

            // 1. Specializations
            Specialization card = new Specialization(); card.setName("Cardiology");
            Specialization ped = new Specialization(); ped.setName("Pediatrics");
            Specialization derm = new Specialization(); derm.setName("Dermatology");
            Specialization neuro = new Specialization(); neuro.setName("Neurology");
            specRepo.saveAll(Arrays.asList(card, ped, derm, neuro));

            // 2. Offices
            Office o1 = new Office(); o1.setName("Office A"); o1.setOfficeNumber(101);
            Office o2 = new Office(); o2.setName("Office B"); o2.setOfficeNumber(102);
            Office o3 = new Office(); o3.setName("Office C"); o3.setOfficeNumber(103);
            officeRepo.saveAll(Arrays.asList(o1, o2, o3));

            // 3. Doctors (referencing already-persisted offices & specializations)
            Doctor d1 = new Doctor();
            d1.setFirstName("John");
            d1.setLastName("Doe");
            d1.setOffice(o1);
            d1.setSpecializations(Arrays.asList(card, ped));

            Doctor d2 = new Doctor();
            d2.setFirstName("Jane");
            d2.setLastName("Smith");
            d2.setOffice(o2);
            d2.setSpecializations(Arrays.asList(derm));

            Doctor d3 = new Doctor();
            d3.setFirstName("Bob");
            d3.setLastName("Brown");

            doctorRepo.saveAll(Arrays.asList(d1, d2, d3));

            // 4. Patients
            Patient p1 = new Patient(); p1.setFirstName("Alice"); p1.setLastName("Uwase");
            Patient p2 = new Patient(); p2.setFirstName("Charlie"); p2.setLastName("Uwase");
            Patient p3 = new Patient(); p3.setFirstName("David"); p3.setLastName("Mugisha");
            Patient p4 = new Patient(); p4.setFirstName("Eve"); p4.setLastName("Keza");
            Patient p5 = new Patient(); p5.setFirstName("Frank"); p5.setLastName("Habimana");
            patientRepo.saveAll(Arrays.asList(p1, p2, p3, p4, p5));

            // 5. Appointments
            Appointment a1 = buildAppt(Date.valueOf("2026-10-05"), "Checkup", AppointmentStatus.SCHEDULED, d1, p1);
            Appointment a2 = buildAppt(Date.valueOf("2026-10-05"), "Follow-up", AppointmentStatus.CONFIRMED, d1, p2);
            Appointment a3 = buildAppt(Date.valueOf("2026-10-10"), "Rash", AppointmentStatus.COMPLETED, d2, p3);
            Appointment a4 = buildAppt(Date.valueOf("2026-10-15"), "Vaccine", AppointmentStatus.CANCELLED, d3, p4);
            Appointment a5 = buildAppt(Date.valueOf("2026-10-20"), "Flu", AppointmentStatus.SCHEDULED, d1, p1);
            Appointment a6 = buildAppt(Date.valueOf("2026-11-01"), "Checkup", AppointmentStatus.SCHEDULED, d2, p5);
            Appointment a7 = buildAppt(Date.valueOf("2026-11-05"), "Injury", AppointmentStatus.CONFIRMED, d1, p3);
            Appointment a8 = buildAppt(Date.valueOf("2026-11-10"), "Allergy", AppointmentStatus.COMPLETED, d2, p1);
            Appointment a9 = buildAppt(Date.valueOf("2026-11-15"), "Consultation", AppointmentStatus.SCHEDULED, d3, p2);
            Appointment a10 = buildAppt(Date.valueOf("2026-11-20"), "Checkup", AppointmentStatus.CANCELLED, d1, p4);

            apptRepo.saveAll(Arrays.asList(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10));

            System.out.println("✅ Database seeded successfully!");
        };
    }

    private Appointment buildAppt(Date date, String reason, AppointmentStatus status, Doctor doc, Patient pat) {
        Appointment a = new Appointment();
        a.setAppointmentDate(date);
        a.setReason(reason);
        a.setStatus(status);
        a.setDoctor(doc);
        a.setPatient(pat);
        return a;
    }
}