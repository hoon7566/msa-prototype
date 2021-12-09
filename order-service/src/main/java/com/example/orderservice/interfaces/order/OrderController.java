package com.example.orderservice.interfaces.order;

import com.example.orderservice.domain.order.Orders;
import com.example.orderservice.interfaces.common.OrderDto;
import com.example.orderservice.applicaion.OrderService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/")
public class OrderController {

    private final Environment env;
    private final OrderService orderService;

    @GetMapping("/welcome")
    public String welcome(){
        return String.format("It's Working in Orders Service"
                +" Port (local.server.port) = " +env.getProperty("local.server.port")
                +" Port (server.port) = " +env.getProperty("server.port")
        );

    }

    @PostMapping(value = "/orders")

    public Mono<ResponseEntity<ResponseOrder>> createOrderReact(@RequestBody RequestOrder requestOrder, @RequestParam String userId) throws IOException, URISyntaxException {
        requestOrder.setUserId(userId);
        OrderDto orderDto = new OrderDto(requestOrder);
        Mono<ResponseOrder> createOrder = orderService.createOrder(orderDto)
                .map(ResponseOrder::new);


        return createOrder
                .map( order ->ResponseEntity.status(HttpStatus.CREATED).body(order) );
    }


    @GetMapping("/orders")
    public Flux<ResponseEntity<ResponseOrder>> retrieveOrders() throws URISyntaxException {

        Flux<ResponseOrder> retrieveOrders = orderService.retrieveOrder()
                .map(ResponseOrder::new);

        return retrieveOrders
                .map( order ->ResponseEntity.ok().body(order) );

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestOrder{
        private Long productId;
        private Integer qty;
        private String userId;
        private Integer unitPrice;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResponseOrder{
        private Long orderId;
        private Long productId;
        private Integer qty;
        private String userId;
        private Integer totalPrice;

        public ResponseOrder(OrderDto orderDto){
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.map(orderDto,this);
        }

    }
}
