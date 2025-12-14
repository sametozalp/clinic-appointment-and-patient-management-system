package com.ozalp.doctor_service.business.dtos.responses;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class DoctorResponse {

    private UUID id;

    private UUID userId;
    private String name;
    private String specialty;
    private boolean active;
    private LocalDateTime createdAt;
}
