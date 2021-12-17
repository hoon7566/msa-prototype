package com.example.paymentservice.interfaces.payment;

import com.example.paymentservice.domain.payment.exception.PaymentException;
import com.example.paymentservice.domain.payment.exception.PaymentErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class PaymentExceptionHandler {

    @ExceptionHandler({PaymentException.class})
    public ResponseEntity<ErrorBody> customExceptionHandler(PaymentException e){
        PaymentErrorEnum errorEnum = e.getErrorEnum();

        log.error("======================================");
        log.error(" error =======>" + errorEnum);
        log.error("======================================");


        return ResponseEntity
                .status(errorEnum.getHttpStatus())
                .body(new ErrorBody(errorEnum.getMessage()));

    }

    @AllArgsConstructor
    @Getter
    static class ErrorBody {
        private String message;
    }
}
