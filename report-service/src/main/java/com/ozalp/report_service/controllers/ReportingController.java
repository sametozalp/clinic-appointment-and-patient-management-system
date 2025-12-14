package com.ozalp.report_service.controllers;

import com.ozalp.report_service.entities.AppointmentReport;
import com.ozalp.report_service.repositories.AppointmentReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
@AllArgsConstructor
public class ReportingController {

    private final AppointmentReportRepository repository;

    @GetMapping("/doctor/{id}")
    public List<AppointmentReport> byDoctor(@PathVariable UUID id) {
        return repository.findByDoctorId(id);
    }
}
