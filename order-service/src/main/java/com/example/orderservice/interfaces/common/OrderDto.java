package com.example.orderservice.interfaces.common;

import com.example.orderservice.domain.order.Orders;
import com.example.orderservice.interfaces.order.OrderController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    private Long orderId;
    private Long productId;
    private Integer qty;
    private String userId;
    private Integer unitPrice;
    private Integer totalPrice;
    private LocalDateTime orderTime;
    private String paymentYn;
    private String deliveryYn;


    public OrderDto(OrderController.RequestOrder requestOrder){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(requestOrder,this);
    }

    public OrderDto(Orders orders){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(orders,this);
    }

}
