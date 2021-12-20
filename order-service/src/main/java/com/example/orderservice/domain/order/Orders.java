package com.example.orderservice.domain.order;


import com.example.orderservice.interfaces.common.OrderDto;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class Orders {

    @Id
    private Long orderId;
    private Long productId;
    private Integer qty;
    private String userId;
    private LocalDateTime orderTime;
    private Integer totalPrice;
    private String paymentYn;
    private String deliveryYn;

    private OrderStatus orderStatus;

    public Orders(OrderDto orderDto){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(orderDto,this);

        this.setTotalPrice(orderDto.getUnitPrice() * orderDto.getQty());
        this.setOrderTime(LocalDateTime.now());
        this.setPaymentYn("N");
        this.setDeliveryYn("N");

    }


}

