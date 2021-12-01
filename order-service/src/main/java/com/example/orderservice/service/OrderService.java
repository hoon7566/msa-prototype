package com.example.orderservice.service;

import com.example.orderservice.client.PaymentServiceClient;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final PaymentServiceClient paymentServiceClient;


    public OrderEntity createOrder(OrderDto orderDto){

        OrderEntity orderEntity = modelMapper.map(orderDto, OrderEntity.class);
        orderEntity.setTotalPrice(orderDto.getUnitPrice() * orderDto.getQty());
        orderEntity.setOrderTime(LocalDateTime.now());
        orderEntity.setPaymentYn("N");
        orderEntity.setDeliveryYn("N");
        OrderEntity createOrderEntity = orderRepository.save(orderEntity);

        //PAYMENT 호출
        Object result = paymentServiceClient.createPayment(orderEntity.getOrderId());

        //DELIVERY 호출

        return createOrderEntity;
    }

    public Iterable<OrderEntity> retrieveOrder(){
        return orderRepository.findAll();
    }

}
