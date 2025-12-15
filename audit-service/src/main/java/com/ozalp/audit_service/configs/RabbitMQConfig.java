package com.ozalp.audit_service.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String APPOINTMENT_EXCHANGE = "appointment.exchange";
    public static final String AUDIT_QUEUE = "audit.queue";
    public static final String AUDIT_DLX = "audit.dlx";
    public static final String AUDIT_DLQ = "audit.dlq";

    @Bean
    public TopicExchange appointmentExchange() {
        return new TopicExchange(APPOINTMENT_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange auditDlx() {
        return new DirectExchange(AUDIT_DLX);
    }

    @Bean
    public Queue auditDlq() {
        return QueueBuilder.durable(AUDIT_DLQ).build();
    }

    @Bean
    public Queue auditQueue() {
        return QueueBuilder
                .durable(AUDIT_QUEUE)
                .withArgument("x-dead-letter-exchange", AUDIT_DLX)
                .withArgument("x-dead-letter-routing-key", AUDIT_DLQ)
                .withArgument("x-max-delivery-count", 3) // 3 kere dene olmazsa dlq
                .build();
    }

    @Bean
    public Binding auditBinding() {
        return BindingBuilder
                .bind(auditQueue())
                .to(appointmentExchange())
                .with("appointment.*");
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder
                .bind(auditDlq())
                .to(auditDlx())
                .with(AUDIT_DLQ);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setDefaultRequeueRejected(false);

        return factory;
    }

}
