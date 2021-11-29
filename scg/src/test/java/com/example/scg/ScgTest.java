package com.example.scg;

import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.HttpHeaders;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.util.Timer;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScgTest {



    @Test
    public void circuitBreakerTest() throws IOException {
        StopWatch stopWatch = new StopWatch("aaaa");
        WebClient client = WebClient.builder()
                .baseUrl("http://localhost:8000/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.USER_AGENT, "Spring 5 WebClient")
                .build();
        for (int i=0;i< 100; i++){

            stopWatch.start("test");
            String test = client.get().uri("/user-service/welcome")
                    .header("Authorization","eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob29uNzU2NiIsImV4cCI6MTYzODI1MzM2OH0.8bVRP7GU7b11ZLZLwJPGUvAmlKLLQlYZRw12uYFEz_m0MH-qYXCaLNjNDSuGHNQZrmK4GvNROmi3M8QC2Eo4ZQ")
                    .retrieve()
                    .bodyToMono(String.class)
                    .flux()
                    .toStream()
                    .findFirst()
                    .orElse("aaaaa");
            stopWatch.stop();
            System.out.println(stopWatch.getTotalTimeMillis());
        }



    }

}
