package com.example.catalogservice.domain.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;


    public Integer getProductStock(Long productId){
        return productRepository.findById(productId)
                .map(products -> products.getStock())
                .orElseThrow(RuntimeException::new);
    }

    @KafkaListener(topics = "${messageQueue.create-order.topic}", groupId = "${messageQueue.create-order.groupId}")
    public void minusStockProduct(String kafkaMessage) throws JsonProcessingException {
        log.info("kafka Message: ->" + kafkaMessage);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<Object,Object> map = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object,Object>>() { });

        Long productId = Long.parseLong(map.get("productId").toString());

        Optional<Products> product = productRepository.findById(productId);

        product.orElseThrow(RuntimeException::new)
                .minusStock();

    }
}
