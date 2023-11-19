package com.express.security.controller;

import com.express.security.dto.AuthenticationRequest;
import com.express.security.dto.CommonResponse;
import com.express.security.entity.UserDetailsApp;
import com.express.security.service.UserService;
import com.express.security.service.impl.CustomUserDetailsImpl;
import com.express.security.service.impl.UserServiceImpl;
import com.express.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsImpl customUserDetails;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        log.info("Login request: {}", request);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails userDetails = customUserDetails.loadUserByUsername(request.getUsername());
        if(Objects.nonNull(userDetails)) {
            String token = jwtUtil.generateToken(userDetails);
            CommonResponse<?> response = CommonResponse.builder()
                    .data("User authenticated successfully")
                    .token(token)
                    .statusCode(CREATED)
                    .build();
            return ResponseEntity.status(CREATED).body(response);
        }

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(INTERNAL_SERVER_ERROR)
                .data("Invalid username or password")
                .build();
      return  ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            log.info("Refresh token request");
            String oldToken = authorizationHeader.substring(7);
            UserDetails userDetails = customUserDetails.loadUserByUsername(jwtUtil.extractUsername(oldToken));

            String accessToken = jwtUtil.generateToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails, request);
            CommonResponse<?> commonResponse = CommonResponse.builder()
                    .token(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            response.setContentType(APPLICATION_JSON_VALUE);
            log.info("Refresh token response: {}", commonResponse.getRefreshToken());
            log.info("Token response: {}", commonResponse.getToken());
            return ResponseEntity.status(CREATED).body(commonResponse);
        }
        throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Invalid token");
    }
}
