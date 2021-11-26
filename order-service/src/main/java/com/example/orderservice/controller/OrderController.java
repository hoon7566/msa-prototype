package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto){

        OrderDto createOrder = orderService.createOrder(orderDto);


        return ResponseEntity.status(HttpStatus.CREATED).body(createOrder);

    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> retrieveOrders(){

        Iterable<OrderEntity> orders = orderService.retrieveOrder();

        List<OrderDto> orderList = new ArrayList<>();

        orders.forEach(e -> orderList.add(modelMapper.map(e, OrderDto.class)));

        return ResponseEntity.status(HttpStatus.OK).body(orderList);

    }
}
