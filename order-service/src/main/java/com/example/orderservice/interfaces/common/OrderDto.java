package com.example.orderservice.interfaces.common;

import com.example.orderservice.domain.order.Orders;
import com.example.orderservice.interfaces.order.OrderController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    private Long productId;
    private Integer qty;
    private String userId;
    private Integer unitPrice;


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
