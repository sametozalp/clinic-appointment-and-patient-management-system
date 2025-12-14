package com.ozalp.appointment_service.business.services;

import com.ozalp.appointment_service.models.dtos.requests.CreateAppointmentRequest;
import com.ozalp.appointment_service.models.dtos.responses.AppointmentResponse;

public interface AppointmentService {

    AppointmentResponse create(CreateAppointmentRequest request);
}
