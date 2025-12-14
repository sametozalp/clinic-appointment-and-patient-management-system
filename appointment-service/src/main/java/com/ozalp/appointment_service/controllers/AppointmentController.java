package com.ozalp.appointment_service.controllers;

import com.ozalp.appointment_service.business.abstracts.AppointmentService;
import com.ozalp.appointment_service.models.dtos.requests.CreateAppointmentRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
@AllArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateAppointmentRequest request) {
        return ResponseEntity.ok(service.create(request));
    }
}
