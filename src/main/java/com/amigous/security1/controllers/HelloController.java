package com.amigous.security1.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HelloController {
    @GetMapping("/hello")
    public ResponseEntity<?> getHelloText() {
        return ResponseEntity.ok("Hello World");
    }

    @GetMapping("/good-bye")
    public ResponseEntity<?> getGoodByeText() {
        return ResponseEntity.ok("Good Bye World");
    }
}
