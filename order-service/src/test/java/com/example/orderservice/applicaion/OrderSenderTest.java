package com.example.orderservice.applicaion;

import com.example.orderservice.interfaces.common.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderResult;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class OrderSenderTest {

    @Autowired
    private OrderSender orderSender;

    @Test
    void send() {

        OrderDto orderDto = OrderDto.builder()
                .orderId(111L)
                .productId(222L)
                .userId("hoon7566").build();

        orderSender.send("aaaa", orderDto);
    }

    @Test
    @DisplayName("kafka message 비동기전송")
    void sendReact() {

        //given
        OrderDto orderDto = OrderDto.builder()
                .orderId(111L)
                .productId(222L)
                .userId("hoon7566").build();

        //when
        Flux<SenderResult<Object>> result= orderSender.sendReact("react" , orderDto);

        //then
        StepVerifier.create(result)
                .assertNext(it ->{
                    log.info(it.toString());
                })
                .verifyComplete();
    }
}