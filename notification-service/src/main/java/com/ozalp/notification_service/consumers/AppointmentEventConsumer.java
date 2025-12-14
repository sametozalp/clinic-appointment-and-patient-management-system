package com.ozalp.notification_service.consumers;

import com.ozalp.notification_service.config.RabbitMQConfig;
import com.ozalp.notification_service.models.AppointmentCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppointmentEventConsumer {

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleAppointmentCreated(AppointmentCreatedEvent event) {

        log.info(
                "NOTIFICATION | Appointment created | appointmentId={} doctorId={} startTime={}",
                event.getAppointmentId(),
                event.getDoctorId(),
                event.getStartTime()
        );
    }
}
