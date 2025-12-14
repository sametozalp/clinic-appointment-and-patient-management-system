package com.ozalp.report_service.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "appointments_reports")
@Getter
@Setter
public class AppointmentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID appointmentId;

    private UUID doctorId;
    private LocalDate date;
    private String status;
}
