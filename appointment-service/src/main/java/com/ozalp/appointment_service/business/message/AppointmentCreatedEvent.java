package com.ozalp.appointment_service.business.message;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentCreatedEvent {

    private UUID eventId;
    private UUID appointmentId;
    private UUID doctorId;
    private UUID patientId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
}
