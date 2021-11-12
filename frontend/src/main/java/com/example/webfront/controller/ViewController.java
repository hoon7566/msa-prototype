package com.example.webfront.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/")
public class ViewController {
    @GetMapping("/")
    public String index(){

        return "index";
    }
    @GetMapping("/health")
    public String health(){

        return "domain/health/health";
    }


}
