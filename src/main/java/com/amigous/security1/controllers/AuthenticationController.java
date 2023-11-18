package com.amigous.security1.controllers;

import com.amigous.security1.config.JwtUtils;
import com.amigous.security1.dao.UserDao;
import com.amigous.security1.models.AuthenticationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserDao userDao;

    @PostMapping(path = "/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        Map<String, Object> response = new HashMap<>();
        UserDetails userDetails = userDao.findUserByEmail(request.getEmail());
        if(Objects.nonNull(userDetails)) {
            String token = jwtUtils.generateToken(userDetails);
             response = Map.of(
                    "data", "User authenticated",
                    "token", token);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        response = Map.of(
            "errors", "Some error has occurred"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }
}
