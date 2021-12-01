package com.example.paymentservice.controller;

import com.example.paymentservice.dto.PaymentDto;
import com.example.paymentservice.jpa.PaymentEntity;
import com.example.paymentservice.service.PaymentService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/")
public class PaymentController {

    private final Environment env;
    private final PaymentService paymentService;
    private final ModelMapper modelMapper;

    @GetMapping("/welcome")
    public String welcome(){
        return String.format("It's Working in Payment Service"
                +" Port (local.server.port) = " +env.getProperty("local.server.port")
                +" Port (server.port) = " +env.getProperty("server.port")
        );

    }

    @PostMapping("/payments")
    public ResponseEntity<ResponsePayment> createPayment(@RequestBody PaymentDto paymentDto){
        PaymentEntity createPaymentEntity = paymentService.createPayment(paymentDto);

        ResponsePayment responsePayment = new ModelMapper().map(createPaymentEntity,ResponsePayment.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responsePayment);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class ResponsePayment{
        private Long paymentId;
        private Long productId;
        private Integer qty;
        private String userId;
        private Integer totalPrice;
    }

    @GetMapping("/payments/{orderId}")
    public ResponseEntity<ResponsePayment> retrievePayments(@PathVariable Long orderId){

        Optional<PaymentEntity> payments = paymentService.getPaymentByOrderId(orderId);

        ResponsePayment responsePayment = payments.isPresent() ? modelMapper.map(payments.get(),ResponsePayment.class) : null;

        return ResponseEntity.status(HttpStatus.OK).body(responsePayment);

    }
}
