package com.example.userservice.domain.user;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/")
public class UserController {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class ResponseUser {
        private String userId;
        private String name;
        private Date createdAt;
    }


    private final Environment env;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request) {

        log.info(String.format("userId = "+request.getParameter("userId")));

        return String.format("It's Working in User Service"
                +" Port (local.server.port) = " +env.getProperty("local.server.port")
                +" Port (server.port) = " +env.getProperty("server.port")
        );

    }

    @GetMapping("/token")
    public ResponseEntity<ResponseUser> token(HttpServletRequest request, @RequestParam String userId) throws Exception {

        ResponseUser responseUser = modelMapper
                .map(   userService.getUserDtoByUserId(userId)
                        ,  ResponseUser.class);

        return ResponseEntity.ok().body(responseUser);

    }

    @PostMapping("/removeToken")
    public ResponseEntity token(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Cookie delCookie = new Cookie("jwt","deleted");
        delCookie.setMaxAge(0);
        response.addCookie(delCookie);

        return ResponseEntity.ok().build();

    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody UserDto userDto){

        UserEntity createUser = userService.createUser(userDto);


        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(createUser,ResponseUser.class));

    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> retrieveUser(){

        Iterable<UserEntity> users= userService.retrieveUser();

        List<ResponseUser> userList = new ArrayList<>();

        users.forEach(e -> userList.add(modelMapper.map(e,ResponseUser.class)));

        return ResponseEntity.status(HttpStatus.OK).body(userList);

    }

    @PutMapping("/users")
    public ResponseEntity updateUser(@RequestBody UserDto userDto){

        userService.updateUser(userDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/users")
    public ResponseEntity deleteUser(@RequestBody UserDto userDto){

        userService.deleteUser(userDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
