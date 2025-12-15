package com.ozalp.appointment_service.business.mappers;

import com.ozalp.appointment_service.entities.Appointment;
import com.ozalp.appointment_service.business.dtos.requests.CreateAppointmentRequest;
import com.ozalp.appointment_service.business.dtos.responses.AppointmentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    Appointment toEntity(CreateAppointmentRequest request);

    AppointmentResponse toResponse(Appointment appointment);
}
