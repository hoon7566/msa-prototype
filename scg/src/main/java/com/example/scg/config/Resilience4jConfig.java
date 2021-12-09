package com.example.scg.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.common.ratelimiter.configuration.RateLimiterConfigCustomizer;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.configure.RateLimiterConfigurationProperties;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.cloud.gateway.filter.factory.FallbackHeadersGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerResilience4JFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.DispatcherHandler;

import java.time.Duration;

@Configuration
@Slf4j
public class Resilience4jConfig {

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> globalCustomConfiguration() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10) //통계건수
                .minimumNumberOfCalls(2) //최소요청횟수
                .failureRateThreshold(60) // 실패율
                .waitDurationInOpenState(Duration.ofMillis(10000)) //breaker 유지시간.
                .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))
                .build();

        return factory ->
                factory.configure(builder -> builder
                .circuitBreakerConfig(config)
                .timeLimiterConfig(timeLimiterConfig)
                .build()
                , "mycb");
    }






}