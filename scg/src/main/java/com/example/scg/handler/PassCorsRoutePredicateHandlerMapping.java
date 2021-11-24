package com.example.scg.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.cloud.gateway.handler.FilteringWebHandler;
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.core.env.Environment;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class PassCorsRoutePredicateHandlerMapping extends RoutePredicateHandlerMapping {


    public PassCorsRoutePredicateHandlerMapping(FilteringWebHandler webHandler, RouteLocator routeLocator,
                                                GlobalCorsProperties globalCorsProperties, Environment environment) {
        super(webHandler, routeLocator, globalCorsProperties, environment);
    }

    @Override
    public Mono<Object> getHandler(ServerWebExchange exchange) {
        logger.info("[PassCorsRoutePredicateHandlerMapping] getHandler");
        return getHandlerInternal(exchange).map(handler -> {
            logger.info(exchange.getLogPrefix() + "Mapped to " + handler);

            // CORS 체크 로직 제거

            return handler;
        });
    }
}
