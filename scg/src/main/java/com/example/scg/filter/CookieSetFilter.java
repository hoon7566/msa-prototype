package com.example.scg.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class CookieSetFilter extends AbstractGatewayFilterFactory<CookieSetFilter.Config> {

    public CookieSetFilter(){
        super(CookieSetFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(CookieSetFilter.Config config) {
        return (exchange, chain) -> chain.filter(exchange)
                .then(Mono.fromRunnable(()-> cookieSet(exchange.getResponse())));
    }

    private void cookieSet(ServerHttpResponse response){
        try{
            List<String> cookies = response.getHeaders().get("Set-Cookie");

            response.getHeaders().remove("Set-Cookie");
            cookies.forEach(s -> {
                ResponseCookie cookie = ResponseCookie.from(s.substring(0,s.indexOf("=")),s.substring(s.indexOf("=")+1))
                        .path("/")
                        .build();
                response.addCookie(cookie);
            });
        }catch (Exception e){
            log.info("not exist cookie");
        }


    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}