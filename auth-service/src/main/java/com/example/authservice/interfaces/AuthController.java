package com.example.authservice.interfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final RedisTemplate<String , String> redisTemplate;


    @GetMapping("/{key}/{value}")
    public String welcome(@PathVariable String key, @PathVariable String value){

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String andSet = valueOperations.getAndSet(key,value);
        return andSet;
    }

    @GetMapping("/{key}")
    public String getValue(@PathVariable String key){

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String andSet = valueOperations.get(key);
        return andSet;
    }

}
