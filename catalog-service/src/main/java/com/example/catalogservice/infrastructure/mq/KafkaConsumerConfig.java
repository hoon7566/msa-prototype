package com.example.catalogservice.infrastructure.mq;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final Environment env;

    @Bean
    public ConsumerFactory<String, Object> consumerProperties(){
        Map<String, Object > properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("messageQueue.server-url"));
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, env.getProperty("messageQueue.create-order.groupId")); //message를 가져갈 consumerGroup 지정
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory(properties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListener( ){
        ConcurrentKafkaListenerContainerFactory<String , String> kafkaListenerContainerFactory
                =new ConcurrentKafkaListenerContainerFactory();
        kafkaListenerContainerFactory
                .setConsumerFactory(
                       consumerProperties()
                );
        return kafkaListenerContainerFactory;
    }
}
