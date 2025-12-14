package com.ozalp.doctor_service.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "doctors")
@Getter
@Setter
public class Doctor {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID userId;
    private String name;
    private String specialty;
    private boolean active;

    private LocalDateTime createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
