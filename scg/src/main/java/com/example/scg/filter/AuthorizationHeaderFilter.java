package com.example.scg.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.AddRequestParameterGatewayFilterFactory;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.support.GatewayToStringStyler;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebSession;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

/**
 * access token는 바로 인증서버를 안거치고 통과시키고,
 * refresh token은 인증서버를 거쳐서 인증절차를 수행후에 통과시키기
 * */


@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    Environment env;

    public static class Config{

    }

    public AuthorizationHeaderFilter(Environment env,AtomicReference<HashSet<String>>  blackList) {
        super(Config.class);
        this.env = env;
    }
    static class Pair{
        String name;
        String value;
        public Pair(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    private ConcurrentHashMap<String , String> validToken(MultiValueMap<String, HttpCookie> cookies) throws Exception{

        ConcurrentHashMap result;

        log.info("=================================token valid start");


        try{
            String accessToken = cookies.get("access-token").get(0).getValue();
            result = validAccessToken(accessToken);
        }catch (Exception e){
            String refreshToken = cookies.get("refresh-token").get(0).getValue();
            result = validRefreshToken(refreshToken);
        }

        return result;

    }

    private ConcurrentHashMap<String , String> validAccessToken(String accessToken) throws Exception{

        if(!isJwtValid(accessToken)) throw new RuntimeException();
        ConcurrentHashMap<String , String> result = new ConcurrentHashMap<>();
        String subject = null;
        subject = Jwts.parser().setSigningKey(env.getProperty("token.secret"))
                .parseClaimsJws(accessToken).getBody()
                .getSubject();
        result.put("data", subject);


        return result;
    }

    private ConcurrentHashMap<String , String> validRefreshToken(String refreshToken){
        ConcurrentHashMap<String , String> result = new ConcurrentHashMap<>();
        WebClient.create("http://localhost:8000/user-service/auth/refresh")
                .get()
                .cookie("refreshToken", refreshToken)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(s -> {
                    log.info("========================result start");
                    result.put("data", s);
                    log.info("========================result end"+ s);
                });

        return result;
    }


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ConcurrentHashMap<String , String> parseTokenMap;
            try{
                parseTokenMap = validToken(request.getCookies());
            }catch (Exception e) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            List<Pair> configList = new ArrayList<>();
            String data = parseTokenMap.get("data");
            configList.add( new Pair("data",data));
            URI newUri = AddParameter(exchange,configList);


            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate().uri(newUri).build();

            //body 데이터 읽기
            //mutatedRequest.getBody()
            // .map(a->a.toString(Charset.forName("UTF-8")))
            // .subscribe(System.out::println);
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }

    private URI AddParameter(ServerWebExchange exchange, List<Pair> configList){
        URI uri = exchange.getRequest().getURI();
        StringBuilder query = new StringBuilder();
        String originalQuery = uri.getRawQuery();
        if (StringUtils.hasText(originalQuery)) {
            query.append(originalQuery);
            if (originalQuery.charAt(originalQuery.length() - 1) != '&') {
                query.append('&');
            }
        }
        configList.stream().forEach(pair -> {
            String value = ServerWebExchangeUtils.expand(exchange, pair.value);
            query.append(pair.name);
            query.append('=');
            query.append(value);
        });

        return UriComponentsBuilder.fromUri(uri).replaceQuery(query.toString()).build(true).toUri();
    }


    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;
        try{
            subject = Jwts.parser().setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(jwt).getBody()
                    .getSubject();
        }catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            returnValue = false;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            returnValue = false;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            returnValue = false;
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            returnValue = false;
        } catch (Exception ex){
            returnValue = false;
        }

        if(subject == null || subject.isEmpty()){
            returnValue= false;
        }


        return returnValue;
    }

    // Mono, Flux -> Spring WebFlux  (Spring f/w 5.0 에서 추가된거)
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse(); // 원래 mvc같은경우면, httpServlet을 사용했겠지만. 이제 더이상 Spring webflux에서는 사용하지않는다.
        response.setStatusCode(httpStatus);
        log.info(err);



        return response.setComplete();
    }

    private ServerWebExchange redirect(ServerWebExchange exchange, String err, HttpStatus httpStatus) {

        ServerHttpRequest mutatedRequest = null;
        try {

            URI newUri =null;
            try {
                newUri = new URI("lb://FRONTEND/view/");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            exchange.getRequest().mutate().uri(newUri);

        }catch (Exception e){
            e.printStackTrace();
        }

        exchange.mutate().request(mutatedRequest).build();
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange;
    }
}
