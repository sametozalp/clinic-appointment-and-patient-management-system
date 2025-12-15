package com.ozalp.appointment_service.business.abstracts;

import com.ozalp.appointment_service.business.dtos.requests.CreateAppointmentRequest;
import com.ozalp.appointment_service.business.dtos.responses.AppointmentResponse;

public interface AppointmentService {

    AppointmentResponse create(CreateAppointmentRequest request);
}
