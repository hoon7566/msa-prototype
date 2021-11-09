package com.example.userservice.controller;

import com.example.userservice.dto.ResponseUser;
import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/")
public class UserController {

    private final Environment env;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/welcome")
    public String welcome(){
        return String.format("It's Working in User Service"
                +" Port (local.server.port) = " +env.getProperty("local.server.port")
                +" Port (server.port) = " +env.getProperty("server.port")
        );

    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody UserDto userDto){

        UserDto createUser = userService.createUser(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(createUser,ResponseUser.class));

    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> retrieveUser(){

        Iterable<UserEntity> users= userService.retrieveUser();

        List<ResponseUser> userList = new ArrayList<>();

        users.forEach(e -> userList.add(modelMapper.map(e,ResponseUser.class)));

        return ResponseEntity.status(HttpStatus.OK).body(userList);

    }
}
