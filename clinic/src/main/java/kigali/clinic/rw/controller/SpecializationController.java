package kigali.clinic.rw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.service.SpecializationService;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationController {

    @Autowired
    private SpecializationService specializationService;

    @PostMapping("/save")
    public ResponseEntity<String> saveSpecialization(@RequestBody Specialization specialization) {
        return new ResponseEntity<>(specializationService.saveSpecialization(specialization), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Specialization> getAll() {
        return specializationService.getAllSpecializations();
    }

    // B3
    @GetMapping("/unused")
    public List<Specialization> getUnused() {
        return specializationService.getUnusedSpecializations();
    }
}