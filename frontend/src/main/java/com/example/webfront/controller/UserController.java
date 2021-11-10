package com.example.webfront.controller;

import com.example.webfront.client.UserServiceClient;
import com.example.webfront.dto.ResponseUser;
import com.example.webfront.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceClient userServiceClient;

    @GetMapping("/")
    public String index(){

        return "index";
    }

    @GetMapping("/registerUser")
    public String userView(){

        return "domain/users/registerUser";
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody UserDto userDto){
        log.info("user :: {}", userDto);

        log.debug(String.format("create User %s",userDto.getUserId()));
        ResponseUser responseUser = userServiceClient.createUser(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);

    }

}
