package com.example.orderservice.interfaces.order;

import com.example.orderservice.applicaion.OrderService;
import com.example.orderservice.interfaces.common.OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.ResultActions;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@WebFluxTest(OrderController.class)
class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webClient;

    @Test
    @DisplayName("주문 성공")
    void createOrder_Success() throws Exception {

        //given
        OrderDto inOrderDto = OrderDto.builder()
                                .productId(1111L)
                                .userId("hoon7566")
                                .qty(30)
                                .unitPrice(3000)
                                .build();
        OrderDto outOrderDto = OrderDto.builder()
                .productId(1111L)
                .userId("hoon7566")
                .qty(30)
                .totalPrice(90000)
                .build();

        OrderController.RequestOrder requestOrder = OrderController.RequestOrder.builder()
                .productId(1111L)
                .userId("hoon7566")
                .qty(30)
                .unitPrice(3000)
                .build();

        OrderController.ResponseOrder responseOrder = OrderController.ResponseOrder.builder()
                .productId(1111L)
                .userId("hoon7566")
                .qty(30)
                .totalPrice(90000)
                .build();



        given(orderService.createOrder(inOrderDto)).willReturn(Mono.just(outOrderDto));

        //when
        WebTestClient.ResponseSpec responseSpec= webClient.post().uri("/orders?userId=hoon7566")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(requestOrder))
                .exchange()
                ;

        //then

        responseSpec.expectStatus().isCreated()
                .expectBody(String.class).isEqualTo(objectMapper.writeValueAsString(responseOrder));
    }

    @Test
    void retrieveOrders() {
    }
}