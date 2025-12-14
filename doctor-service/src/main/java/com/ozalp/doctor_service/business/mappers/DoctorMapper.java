package com.ozalp.doctor_service.business.mappers;

import com.ozalp.doctor_service.business.dtos.requests.CreateDoctorRequest;
import com.ozalp.doctor_service.business.dtos.responses.DoctorResponse;
import com.ozalp.doctor_service.entities.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorResponse toResponse(Doctor doctor);

    Doctor toEntity(CreateDoctorRequest request);
}
