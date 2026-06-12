package com.restaurant.booking.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.reports}")
    private String reportsTopic;

    @Bean
    public NewTopic reportsTopic() {
        return TopicBuilder.name(reportsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}