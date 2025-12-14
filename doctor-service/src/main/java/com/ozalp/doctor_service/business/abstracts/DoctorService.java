package com.ozalp.doctor_service.business.abstracts;

import com.ozalp.doctor_service.business.dtos.requests.CreateDoctorRequest;
import com.ozalp.doctor_service.business.dtos.responses.DoctorResponse;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    DoctorResponse create(CreateDoctorRequest request);

    List<DoctorResponse> getAll();

    void deactivate(UUID doctorId);
}
