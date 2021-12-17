package com.example.paymentservice.domain.payment.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PaymentErrorEnum {
    NOT_EXIST_PAYMENT(HttpStatus.OK, "Not exist Payment"),
    DUP_PAYMENT(HttpStatus.CONFLICT, "Dup Payment"),
    PAYMENT_INVALID(HttpStatus.CONFLICT, "INVALID Payment");

    private final HttpStatus httpStatus;
    private final String message;
}
