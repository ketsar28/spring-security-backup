//package com.express.security.controller;
//
//import com.express.security.model.AuthRequest;
//import com.express.security.model.LoginResponse;
//import com.express.security.entity.User;
//import com.express.security.service.impl.AuthenticationServiceImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/auth")
//@CrossOrigin("*")
//public class AuthenticationController {
//
//    private final AuthenticationServiceImpl authenticationService;
//
//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@RequestBody AuthRequest request) {
//        User user = authenticationService.registerUser(request.getUsername(), request.getPassword());
//        return ResponseEntity.status(HttpStatus.CREATED).body(user);
//    }
//
//    @GetMapping
//    public ResponseEntity<?> getAllUsers() {
//        List<User> users = authenticationService.getAllUser();
//        return ResponseEntity.ok(users);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> loginUser(@RequestBody AuthRequest request) {
//        LoginResponse loginResponse = authenticationService.loginUser(request.getUsername(), request.getPassword());
//
//        if (loginResponse.getUser() != null) {
//            return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }
//}
