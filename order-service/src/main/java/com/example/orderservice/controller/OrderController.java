package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.service.OrderService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        return String.format("It's Working in Order Service"
                +" Port (local.server.port) = " +env.getProperty("local.server.port")
                +" Port (server.port) = " +env.getProperty("server.port")
        );

    }

    @PostMapping("/orders")
    public ResponseEntity<ResponseOrder> createOrder(@RequestBody OrderDto orderDto, @RequestParam String userId) throws IOException {
        orderDto.setUserId(userId);
        OrderEntity createOrderEntity = orderService.createOrder(orderDto);

        ResponseOrder responseOrder = new ModelMapper().map(createOrderEntity,ResponseOrder.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
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

    @GetMapping("/orders")
    public ResponseEntity<List<ResponseOrder>> retrieveOrders(){

        Iterable<OrderEntity> orders = orderService.retrieveOrder();

        List<ResponseOrder> orderList = new ArrayList<>();

        orders.forEach(e -> orderList.add(modelMapper.map(e, ResponseOrder.class)));

        return ResponseEntity.status(HttpStatus.OK).body(orderList);

    }
}
