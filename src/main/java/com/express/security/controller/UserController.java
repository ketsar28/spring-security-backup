package com.express.security.controller;

import com.express.security.model.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    @GetMapping
    public ResponseEntity<?> getHelloUser(@RequestParam(name = "name") String username) {
        CommonResponse<?> response = CommonResponse.builder()
                .data("hello " + username)
                .statusCode(HttpStatus.OK.toString())
                .build();
        return ResponseEntity.ok(response);
    }

}
