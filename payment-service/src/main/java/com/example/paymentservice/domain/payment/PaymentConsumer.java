package com.example.paymentservice.domain.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentConsumer {
    private final PaymentRepository paymentRepository;
    private final EntityManager entityManager;
    private final Environment env;


    @KafkaListener(topics = "${messageQueue.create-order.topic}", groupId = "${messageQueue.create-order.groupId}")
    public void createPayment(String kafkaMessage) throws JsonProcessingException {
        log.info("kafka Message: ->" + kafkaMessage);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<Object,Object> map = new HashMap<>();

        try{
            map = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object,Object>>() { });
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        PaymentEntity payment = PaymentEntity.builder()
                .orderId(Long.parseLong(map.get("orderId").toString()))
                .price(Long.parseLong(map.get("totalPrice").toString()))
                .errorYn("N")
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);


    }
}
