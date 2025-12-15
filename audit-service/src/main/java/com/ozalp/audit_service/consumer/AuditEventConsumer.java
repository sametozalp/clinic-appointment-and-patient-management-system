package com.ozalp.audit_service.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozalp.audit_service.entities.AuditLog;
import com.ozalp.audit_service.message.AppointmentCreatedEvent;
import com.ozalp.audit_service.repositories.AuditRepository;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.ozalp.audit_service.configs.RabbitMQConfig.AUDIT_QUEUE;

@Component
@AllArgsConstructor
@Slf4j
public class AuditEventConsumer {

    private final AuditRepository repository;
    private final ObjectMapper mapper;
    private final Jackson2JsonMessageConverter jackson2JsonMessageConverter;

//    @RabbitListener(queues = AUDIT_QUEUE)
//    public void consume(AppointmentCreatedEvent event) throws Exception {
//
//        AuditLog logEntry = new AuditLog();
//        logEntry.setEventType(event.getClass().getSimpleName());
//        logEntry.setCreatedAt(LocalDateTime.now());
//        logEntry.setPayload(mapper.writeValueAsString(event));
//
//        repository.save(logEntry);
//
//        log.info("AUDIT LOG SAVED");
//    }

    @RabbitListener(queues = AUDIT_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void consume(AppointmentCreatedEvent event, Channel channel, Message message) throws IOException {

        try {
            AuditLog log = new AuditLog();
            log.setEventType("AppointmentCreated");
            log.setPayload(event.toString());
            log.setCreatedAt(LocalDateTime.now());

            repository.save(log);

            channel.basicAck(
                    message.getMessageProperties().getDeliveryTag(),
                    false
            );

        } catch (Exception ex) {

            log.error("AUDIT FAILED", ex);

            channel.basicNack(
                    message.getMessageProperties().getDeliveryTag(),
                    false,
                    false // requeue = false → DLQ
            );
        }
    }
}
