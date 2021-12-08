package com.example.orderservice.service;

import com.example.orderservice.applicaion.OrderService;
import com.example.orderservice.interfaces.common.OrderDto;
import com.example.orderservice.domain.order.Orders;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@Slf4j
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Test
    public void createOrder(){

        OrderDto orderDto = OrderDto.builder()
                .productId(1001L)
                .qty(10)
                .unitPrice(1500)
                .build();
        Mono<OrderDto> orderMono =  orderService.createOrder(orderDto);

        StepVerifier.create(orderMono)
                .assertNext(it ->{
                    log.info(it.toString());
                    Assertions.assertEquals(orderDto.getProductId(), it.getProductId());
                    Assertions.assertEquals(orderDto.getQty(), it.getQty());
                })
                .verifyComplete();

    }
}