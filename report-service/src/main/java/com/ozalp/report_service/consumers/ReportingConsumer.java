package com.ozalp.report_service.consumers;

import com.ozalp.report_service.entities.AppointmentReport;
import com.ozalp.report_service.models.AppointmentCreatedEvent;
import com.ozalp.report_service.repositories.AppointmentReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.ozalp.report_service.configs.RabbitMQConfig.REPORTING_QUEUE;

@Component
@RequiredArgsConstructor
public class ReportingConsumer {

    private final AppointmentReportRepository repository;

    @RabbitListener(queues = REPORTING_QUEUE)
    public void consume(AppointmentCreatedEvent event) {

        AppointmentReport r = new AppointmentReport();
        r.setAppointmentId(event.getAppointmentId());
        r.setDoctorId(event.getDoctorId());
        r.setDate(event.getStartTime().toLocalDate());
        r.setStatus("CREATED");

        repository.save(r);
    }
}
