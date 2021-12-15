package com.example.orderservice.applicaion;

import com.example.orderservice.interfaces.common.OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderSender {

    private final KafkaSender<String , Object> kafkaSender;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderDto send(String topic , OrderDto orderDto){
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try{
            jsonInString = mapper.writeValueAsString(orderDto);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("kafka Producer sent data from the Order microservice: "+ orderDto);

        return orderDto;

    }

    public Flux<SenderResult<Object>> sendReact(String topic , OrderDto orderDto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(orderDto);

        Mono<SenderRecord<String,Object,Object>> outRecordMono =
                Mono.just(SenderRecord.create(topic,
                        null,
                        System.currentTimeMillis(),
                        null,
                        jsonInString,
                        null));

        return kafkaSender.send(outRecordMono)
                .doOnError(Throwable::printStackTrace)
                .doOnComplete(() -> log.info("send success"));

    }


}
