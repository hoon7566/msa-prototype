package com.example.orderservice.applicaion;

import com.example.orderservice.interfaces.common.OrderDto;
import com.example.orderservice.domain.order.Orders;
import com.example.orderservice.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderSender orderSender;
    private final Environment env;



    public Mono<OrderDto> createOrder(OrderDto orderDto){

        Orders order = new Orders(orderDto);

        Mono<OrderDto> createOrder = orderRepository.save(order)
                .map(orders -> {
                    OrderDto createOrderDto = new OrderDto(orders);
                    //kafka로 order전송
                    orderSender.send(env.getProperty("topic.order.create"),createOrderDto);
                    return new OrderDto(orders);
                });

        return createOrder;
    }

    @Transactional(readOnly = true)
    public Flux<OrderDto> retrieveOrder(){
        return orderRepository.findAll().map(orders -> new OrderDto(orders));
    }


}
