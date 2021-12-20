package com.example.orderservice.domain.order;

import com.example.orderservice.applicaion.OrderServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderBeforeSaveCallback implements BeforeSaveCallback<Orders> {

    private final OrderServiceClient orderServiceClient;

    @Override
    public Publisher<Orders> onBeforeSave(Orders entity, OutboundRow row, SqlIdentifier table) {

        Integer productStock = orderServiceClient.productStock(entity.getProductId());
        log.info("entity.getProductId() ===> 재고수량 :" +productStock);
        if(productStock<= 0) throw new RuntimeException("재고가 없음미ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");


        return Mono.just(entity);
    }
}
