package com.example.webfront.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/")
public class UserController {

    @GetMapping("/")
    public String index(){

        return "index";
    }

    @GetMapping("/registerUser")
    public String userView(){

        return "domain/users/registerUser";
    }

    @PostMapping("/users")
    public void createUser(){

    }

}
