package com.example.paymentservice.domain.payment.exception;


import lombok.Getter;

@Getter
public class PaymentException extends RuntimeException{

    private final PaymentErrorEnum errorEnum;
    public PaymentException(PaymentErrorEnum errorEnum){
        super(errorEnum.getMessage());
        this.errorEnum = errorEnum;
    }
}
