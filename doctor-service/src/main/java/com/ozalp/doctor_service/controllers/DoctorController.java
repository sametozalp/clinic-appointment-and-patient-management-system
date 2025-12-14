package com.ozalp.doctor_service.controllers;

import com.ozalp.doctor_service.business.abstracts.DoctorService;
import com.ozalp.doctor_service.business.dtos.requests.CreateDoctorRequest;
import com.ozalp.doctor_service.entities.Doctor;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctors")
@AllArgsConstructor
public class DoctorController {

    private final DoctorService service;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateDoctorRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivate(@PathVariable UUID id) {
        service.deactivate(id);
        return ResponseEntity.ok(true);
    }
}
