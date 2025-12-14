package com.ozalp.audit_service.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "logs")
public class AuditLog {

    @Id
    @GeneratedValue
    private UUID id;

    private String eventType;
    private UUID entityId;
    private UUID actorId;
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String payload;
}
