package com.ozalp.appointment_service.entities;

import com.ozalp.appointment_service.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments",
        indexes = {
                @Index(name = "idx_doctor_time", columnList = "doctorId,startTime,endTime")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID patientId;
    private UUID doctorId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    private LocalDateTime createAt;

    @PrePersist
    private void onCreate() {
        this.createAt = LocalDateTime.now();
    }
}
