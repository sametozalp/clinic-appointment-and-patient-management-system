package com.ozalp.appointment_service.business.concretes;

import com.ozalp.appointment_service.business.abstracts.AppointmentEventProducerService;
import com.ozalp.appointment_service.business.message.AppointmentCreatedEvent;
import com.ozalp.appointment_service.config.RabbitMQConfig;
import com.ozalp.appointment_service.entities.Appointment;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@AllArgsConstructor
public class AppointmentEventProducerManager implements AppointmentEventProducerService {

    private final RabbitTemplate rabbitTemplate;

    public void sendAppointmentCreated(Appointment appointment) {

        AppointmentCreatedEvent event = AppointmentCreatedEvent.builder()
                .eventId(UUID.randomUUID())
                .appointmentId(appointment.getId())
                .doctorId(appointment.getDoctorId())
                .patientId(appointment.getPatientId())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .createdAt(LocalDateTime.now())
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.APPOINTMENT_EXCHANGE,
                RabbitMQConfig.APPOINTMENT_CREATED_ROUTING_KEY,
                event, message -> {
                    message.getMessageProperties()
                            .setDeliveryMode(MessageDeliveryMode.PERSISTENT); // mesajı diske yazma. sever çökse bile mesaj durur
                    return message;
                }
        );
    }
}
