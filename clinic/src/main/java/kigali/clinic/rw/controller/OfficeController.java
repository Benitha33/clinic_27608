package kigali.clinic.rw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.service.OfficeService;

@RestController
@RequestMapping(value = "/api/offices")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveOffice(@RequestBody Office office) {
        String returnedMessage = officeService.saveOffice(office);
        return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
    }

    // C3: Busiest office
    @GetMapping("/busiest")
    public ResponseEntity<?> getBusiestOffice() {
        List<Object[]> result = officeService.getBusiestOffice();
        if (result.isEmpty()) {
            return ResponseEntity.ok("No appointments yet");
        }
        return ResponseEntity.ok(result.get(0));
    }
}
