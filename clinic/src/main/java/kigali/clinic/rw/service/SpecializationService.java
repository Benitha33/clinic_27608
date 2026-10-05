package kigali.clinic.rw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepo;

    public String saveSpecialization(Specialization specialization) {
        specializationRepo.save(specialization);
        return "Specialization saved successfully";
    }

    public List<Specialization> getAllSpecializations() {
        return specializationRepo.findAll();
    }

    // B3
    public List<Specialization> getUnusedSpecializations() {
        return specializationRepo.findUnusedSpecializations();
    }
}