package com.example.paymentservice.interfaces.common;

import com.example.paymentservice.domain.payment.PaymentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@Data
@AllArgsConstructor
@Builder
public class PaymentDto {

    private Long orderId;
    private String errorId;
    private Integer price;
    private String paymentValidateToken;


    protected PaymentDto(){}

    public PaymentDto(PaymentEntity paymentEntity){
      ModelMapper modelMapper = new ModelMapper();
      modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
      modelMapper.map(this, paymentEntity);

    }

}
