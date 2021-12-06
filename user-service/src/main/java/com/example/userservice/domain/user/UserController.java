package com.example.userservice.domain.user;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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

        ResponseUser responseUser = new ResponseUser(userService.getUserDtoByUserId(userId));

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
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser requestUser){
        UserDto userDto = new UserDto(requestUser);
        UserDto createUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseUser(createUser));
    }

    @GetMapping("/users")
    public ResponseEntity<Result> retrieveUser(){

        List<UserDto> users= userService.retrieveUser();
        List<ResponseUser> userList = new ArrayList<>();
        users.forEach(e -> userList.add(new ResponseUser(e)));
        Result<List<ResponseUser>> result = new Result<>(userList.size(),userList);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @PutMapping("/users")
    public ResponseEntity updateUser(@RequestBody RequestUser requestUser){
        userService.updateUser(new UserDto(requestUser));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/users")
    public ResponseEntity deleteUser(@RequestBody RequestUser requestUser){
        userService.deleteUser(new UserDto(requestUser));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class ResponseUser  {
        private String userId;
        private String name;
        private Date createdAt;

        public ResponseUser ( UserDto userDto){
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.map(userDto , this);
        }

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class RequestUser {
        private String userId;
        private String name;
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }


}
