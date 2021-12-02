package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.react.Orders;
import com.example.orderservice.service.OrderService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/")
public class OrderController {

    private final Environment env;
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @GetMapping("/welcome")
    public String welcome(){
        return String.format("It's Working in Orders Service"
                +" Port (local.server.port) = " +env.getProperty("local.server.port")
                +" Port (server.port) = " +env.getProperty("server.port")
        );

    }

    @PostMapping(value = "/orders" )
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<ResponseOrder>> createOrderReact(@RequestBody OrderDto orderDto, @RequestParam String userId) throws IOException, URISyntaxException {
        orderDto.setUserId(userId);
        Mono<ResponseOrder> createOrder = orderService.createOrder(orderDto)
                .map(orders -> new ModelMapper().map(orders,ResponseOrder.class));
        HttpHeaders httpHeaders = new HttpHeaders();

        //TODO:orderId를 가져와서 URI를 만들어야함.
        URI newUri = new URI("/view/order");
        httpHeaders.setLocation(newUri);

        return createOrder
                .map( order ->ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(order) );
    }


    @GetMapping("/orders")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ResponseEntity<ResponseOrder>> retrieveOrders() throws URISyntaxException {

        Flux<ResponseOrder> retrieveOrders = orderService.retrieveOrder()
                .map(orders -> new ModelMapper().map(orders,ResponseOrder.class));

        return retrieveOrders
                .map( order ->ResponseEntity.ok().body(order) );

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class ResponseOrder{
        private Long orderId;
        private Long productId;
        private Integer qty;
        private String userId;
        private Integer totalPrice;
    }
}
