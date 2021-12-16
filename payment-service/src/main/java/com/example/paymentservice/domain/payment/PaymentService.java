package com.example.paymentservice.domain.payment;

import com.example.paymentservice.domain.payment.exception.PaymentErrorEnum;
import com.example.paymentservice.domain.payment.exception.PaymentException;
import com.example.paymentservice.interfaces.common.PaymentDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    private boolean validateToken(String token){
        //TODO: 결제 token validate 들어가야함.
        return true;
    }

    public PaymentDto createPayment(PaymentDto paymentDto) {


        if(!validateToken(paymentDto.getPaymentValidateToken())){
            throw new RuntimeException("결제 안된건이오.");
        }

        PaymentEntity paymentEntity = modelMapper.map(paymentDto, PaymentEntity.class);
        paymentEntity.setCreatedAt(LocalDateTime.now());
        PaymentEntity createPaymentEntity = paymentRepository.save(paymentEntity);

        //DELIVERY 호출

        return new PaymentDto(createPaymentEntity);
    }

    @Transactional(readOnly = true)
    public PaymentDto getPaymentByOrderId(Long orderId) {
        return paymentRepository
                .findByOrderId(orderId)
                .map(paymentEntity -> new PaymentDto(paymentEntity))
                .orElseThrow( () -> new PaymentException(PaymentErrorEnum.NOT_EXIST_PAYMENT));
    }


    @KafkaListener(topics = "${messageQueue.create-order.topic}", groupId = "${messageQueue.create-order.groupId}")
    public void createPayment(String kafkaMessage) throws JsonProcessingException {
        log.info("kafka Message: ->" + kafkaMessage);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<Object,Object> map = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object,Object>>() { });

        PaymentEntity payment = PaymentEntity.builder()
                .orderId(Long.parseLong(map.get("orderId").toString()))
                .price(Long.parseLong(map.get("totalPrice").toString()))
                .errorYn("N")
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

    }

}
