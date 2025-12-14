package com.ozalp.appointment_service.mappers;

import com.ozalp.appointment_service.entities.Appointment;
import com.ozalp.appointment_service.models.dtos.requests.CreateAppointmentRequest;
import com.ozalp.appointment_service.models.dtos.responses.AppointmentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    Appointment toEntity(CreateAppointmentRequest request);

    AppointmentResponse toResponse(Appointment appointment);
}
