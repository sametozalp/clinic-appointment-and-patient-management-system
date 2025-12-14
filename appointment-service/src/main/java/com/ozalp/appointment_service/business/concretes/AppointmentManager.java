package com.ozalp.appointment_service.business.concretes;

import com.ozalp.appointment_service.business.abstracts.AppointmentService;
import com.ozalp.appointment_service.dataAccess.AppointmentRepository;
import com.ozalp.appointment_service.entities.Appointment;
import com.ozalp.appointment_service.enums.AppointmentStatus;
import com.ozalp.appointment_service.mappers.AppointmentMapper;
import com.ozalp.appointment_service.messaging.producer.AppointmentEventProducer;
import com.ozalp.appointment_service.models.dtos.requests.CreateAppointmentRequest;
import com.ozalp.appointment_service.models.dtos.responses.AppointmentResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AppointmentManager implements AppointmentService {

    private final AppointmentRepository repository;
    private final AppointmentEventProducer eventProducer;
    private final AppointmentMapper mapper;

    @Transactional
    public AppointmentResponse create(CreateAppointmentRequest request) {
        Appointment appointment = mapper.toEntity(request);

        boolean exists = repository.existsByDoctorIdAndStartTimeLessThanAndEndTimeGreaterThan(
                appointment.getDoctorId(),
                appointment.getEndTime(),
                appointment.getStartTime()
        );

        if (exists) {
            throw new RuntimeException("Doctor already has appointment");
        }

        appointment.setStatus(AppointmentStatus.SCHEDULED);
        Appointment saved = repository.save(appointment);

        eventProducer.sendAppointmentCreated(saved);

        return mapper.toResponse(saved);
    }
}
