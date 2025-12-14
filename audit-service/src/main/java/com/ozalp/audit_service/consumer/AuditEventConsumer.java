package com.ozalp.audit_service.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozalp.audit_service.entities.AuditLog;
import com.ozalp.audit_service.repositories.AuditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
@Slf4j
public class AuditEventConsumer {

    private final AuditRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    @RabbitListener(queues = "audit.queue")
    public void consume(Object event) throws Exception {

        AuditLog logEntry = new AuditLog();
        logEntry.setEventType(event.getClass().getSimpleName());
        logEntry.setCreatedAt(LocalDateTime.now());
        logEntry.setPayload(mapper.writeValueAsString(event));

        repository.save(logEntry);

        log.info("AUDIT LOG SAVED");
    }
}
