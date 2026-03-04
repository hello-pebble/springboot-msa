package com.pebble.auth.adapter.in.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    public static void main(String[] args) {
        // 외부 요청을 받는 진입점
        System.out.print("Hello and welcome! :: AuthController");

    }

    @PostMapping("/api/v1/users/signup")
    public void signup(){
        System.out.print("signup start");

    }
}