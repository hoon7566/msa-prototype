package com.example.scg.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
    public static class Config{
        // Put the configuration properties
    }

    public CustomFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Custom pre Filter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest(); // reactive포함된거로 import
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom PRE com.example.scg.filter: request id -> {}", request.getId());

            // Custom Post Filter
            //Mono는 webflux에서 단일값 전송할때 Mono값으로 전송
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                log.info("Custom POST com.example.scg.filter: response code -> {}", response.getStatusCode());
            }));

        };
    }
}
