package com.ozalp.report_service.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String APPOINTMENT_EXCHANGE = "appointment.exchange";
    public static final String REPORTING_QUEUE = "reporting.queue";
    public static final String REPORT_DLX = "report.dlx";
    public static final String REPORT_DLQ = "report.dlq";

    @Bean
    public DirectExchange reportDlx() {
        return new DirectExchange(REPORT_DLX);
    }

    @Bean
    public TopicExchange appointmentExchange() {
        return new TopicExchange(APPOINTMENT_EXCHANGE);
    }

    @Bean
    public Queue reportDlq() {
        return QueueBuilder.durable(REPORT_DLQ).build();
    }

    @Bean
    public Queue reportingQueue() {
        return QueueBuilder
                .durable(REPORTING_QUEUE)
                .withArgument("x-dead-letter-exchange", REPORT_DLX)
                .withArgument("x-dead-letter-routing-key", REPORT_DLQ)
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
    public Binding dlqBinding() {
        return BindingBuilder
                .bind(reportDlq())
                .to(reportDlx())
                .with(REPORT_DLQ);
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
        factory.setMessageConverter(jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setDefaultRequeueRejected(false);

        return factory;
    }

}
