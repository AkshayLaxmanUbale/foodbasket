package com.demo.foodbasket.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {
    
    // https://localhost:8080/home/users
    @GetMapping("/users")
    public String getUsers() {
        return "Users";
    }
}
