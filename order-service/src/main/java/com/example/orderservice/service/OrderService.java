package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    public OrderDto createOrder(OrderDto orderDto){

        OrderEntity orderEntity = modelMapper.map(orderDto, OrderEntity.class);

        OrderEntity createOrderEntity = orderRepository.save(orderEntity);

        return modelMapper.map(createOrderEntity, OrderDto.class);
    }

    public Iterable<OrderEntity> retrieveOrder(){
        return orderRepository.findAll();
    }

}
