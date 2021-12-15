package com.example.paymentservice.domain.payment;

import com.example.paymentservice.interfaces.common.PaymentDto;
import com.example.paymentservice.domain.payment.PaymentEntity;
import com.example.paymentservice.domain.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    private boolean validateToken(String token){
        //TODO: 결제 token validate 들어가야함.
        return true;
    }

    public PaymentEntity createPayment(PaymentDto paymentDto) {


        if(!validateToken(paymentDto.getPaymentValidateToken())){
            throw new RuntimeException("결제 안된건이오.");
        }


        PaymentEntity paymentEntity = modelMapper.map(paymentDto, PaymentEntity.class);
        paymentEntity.setCreatedAt(LocalDateTime.now());
        PaymentEntity createPaymentEntity = paymentRepository.save(paymentEntity);

        //DELIVERY 호출

        return createPaymentEntity;
    }

    public Optional<PaymentEntity> getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

}
