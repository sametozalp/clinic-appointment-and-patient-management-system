package com.ozalp.doctor_service.business.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateDoctorRequest {

    private UUID userId;
    private String name;
    private String specialty;
}
