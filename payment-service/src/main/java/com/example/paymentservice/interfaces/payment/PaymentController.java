package com.example.paymentservice.interfaces.payment;

import com.example.paymentservice.interfaces.common.PaymentDto;
import com.example.paymentservice.domain.payment.PaymentEntity;
import com.example.paymentservice.domain.payment.PaymentService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        PaymentDto createPaymentEntity = paymentService.createPayment(paymentDto);

        ResponsePayment responsePayment = new ModelMapper().map(createPaymentEntity,ResponsePayment.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responsePayment);
    }



    @GetMapping("/payments/{orderId}")
    public ResponseEntity<ResponsePayment> getPayment(@PathVariable Long orderId){

        PaymentDto payments = paymentService.getPaymentByOrderId(orderId);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponsePayment(payments));

    }

    @DeleteMapping("/payments/{orderId}")
    public ResponseEntity removePayments(@PathVariable Long orderId){
        paymentService.removePayment(orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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

        public ResponsePayment(PaymentDto paymentDto){
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.map(this, paymentDto);
        }
    }

}
