package com.example.ViChat.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers:-localhost}")
    private String bootstrapServers;
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(){
        return new KafkaTemplate<String, Object>(producerFactory());
    }

    @Bean
    public NewTopic confirmAccount() {
        return new NewTopic("confirm-account-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic forgotPassword(){
        return new NewTopic("forgot-password-topic", 3, (short) 1);
    }
}
