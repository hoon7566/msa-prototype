package com.example.orderservice.infrastructure.mq;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public Map<String, Object> producerProperties(){
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return properties;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(){
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerProperties()));
    }

    @Bean
    public SenderOptions< String, Object> senderOptions(){
        SenderOptions< String, Object> senderOptions =
                SenderOptions.<String,Object>create(producerProperties())
                        .maxInFlight(1024);
        return senderOptions;
    }

    @Bean
    public KafkaSender<String , Object> kafkaSender(){
        return KafkaSender.create(senderOptions());
    }
}
