package com.ozalp.appointment_service.business.abstracts;

import com.ozalp.appointment_service.entities.Appointment;

public interface AppointmentEventProducerService {

    void sendAppointmentCreated(Appointment appointment);
}
