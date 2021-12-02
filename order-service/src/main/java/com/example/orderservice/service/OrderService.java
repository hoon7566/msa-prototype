package com.example.orderservice.service;

import com.example.orderservice.client.PaymentServiceClient;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.react.Orders;
import com.example.orderservice.react.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final PaymentServiceClient paymentServiceClient;


    public Mono<Orders> createOrder(OrderDto orderDto){

        Orders order = modelMapper.map(orderDto, Orders.class);
        order.setTotalPrice(orderDto.getUnitPrice() * orderDto.getQty());
        order.setOrderTime(LocalDateTime.now());
        order.setPaymentYn("N");
        order.setDeliveryYn("N");
        Mono<Orders> createOrder = orderRepository.save(order);

        return createOrder;
    }

    public Flux<Orders> retrieveOrder(){
        return orderRepository.findAll();
    }

}
