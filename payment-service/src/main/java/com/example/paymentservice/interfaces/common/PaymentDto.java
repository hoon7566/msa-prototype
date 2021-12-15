package com.example.paymentservice.interfaces.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {

    private Long orderId;
    private String errorId;
    private Integer price;
    private String paymentValidateToken;

}
