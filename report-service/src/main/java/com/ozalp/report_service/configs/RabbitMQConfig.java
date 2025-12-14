package com.ozalp.report_service.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String APPOINTMENT_EXCHANGE = "appointment.exchange";
    public static final String REPORTING_QUEUE = "reporting.queue";

    @Bean
    public TopicExchange appointmentExchange() {
        return new TopicExchange(APPOINTMENT_EXCHANGE);
    }

    @Bean
    public Queue reportingQueue() {
        return QueueBuilder
                .durable(REPORTING_QUEUE)
                .build();
    }

    @Bean
    public Binding reportingBinding() {
        return BindingBuilder
                .bind(reportingQueue())
                .to(appointmentExchange())
                .with("appointment.created");
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
