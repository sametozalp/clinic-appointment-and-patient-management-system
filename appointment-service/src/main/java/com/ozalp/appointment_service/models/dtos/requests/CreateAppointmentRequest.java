package com.ozalp.appointment_service.models.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CreateAppointmentRequest {

    private UUID patientId;
    private UUID doctorId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
