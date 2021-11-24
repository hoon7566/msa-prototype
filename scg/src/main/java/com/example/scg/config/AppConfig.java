package com.example.scg.config;

import com.example.scg.handler.PassCorsRoutePredicateHandlerMapping;
import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.cloud.gateway.handler.FilteringWebHandler;
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

@Configuration
public class AppConfig {

    @Bean
    public AtomicReference<HashSet<String>> blackList(){
        AtomicReference<HashSet<String>> blackList = new AtomicReference();
        blackList.set(new HashSet<String>());
        return blackList;
    }

    @Bean
    @Primary
    public RoutePredicateHandlerMapping passCorsRoutePredicateHandlerMapping(
            FilteringWebHandler webHandler, RouteLocator routeLocator,
            GlobalCorsProperties globalCorsProperties, Environment environment) {
        return new PassCorsRoutePredicateHandlerMapping(webHandler, routeLocator,
                globalCorsProperties, environment);
    }
}
