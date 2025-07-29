//package com.epam.edp.demo.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMQConfig {
//
//    @Value("${rabbitmq.queue.name}")
//    private String queueName;
//
//    @Value("${rabbitmq.exchange.name}")
//    private String exchangeName;
//
//    @Value("${rabbitmq.routing.key}")
//    private String routingKey;
//
//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange(exchangeName);
//    }
//
//    @Bean
//    public Queue queue() {
//        return QueueBuilder.durable(queueName).build();
//    }
//
//    @Bean
//    public Binding binding(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
//    }
//}
