package com.amigous.security2.controller;

import com.amigous.security2.entity.Role;
import com.amigous.security2.entity.User;
import com.amigous.security2.filter.CustomAuthenticationFilter;
import com.amigous.security2.model.RoleToUserRequest;
import com.amigous.security2.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        Map<String, Object> response = Map.of("data", userService.getAllUsers());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/save")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        Map<String, Object> response = Map.of("data", userService.saveUser(user));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable("username") String username) {
        Map<String, Object> response = Map.of("data", userService.getUser(username));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/role/save")
    public ResponseEntity<?> addRole(@RequestBody Role role) {
        Map<String, Object> response = Map.of("data", userService.saveRole(role));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/role/add-to-user")
    public ResponseEntity<?> saveRoleToUser(@RequestBody RoleToUserRequest request) {
        userService.addRoleToUser(request.getUsername(), request.getRoleName());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("data", "Role added to user"));
    }

    @GetMapping("/token/refresh")
    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                DecodedJWT verify = JWT.require(algorithm).build().verify(refreshToken);
                String username = verify.getSubject();
                User user = userService.getUser(username);

                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new java.util.Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = Map.of(
                        "access_token", accessToken,
                        "refresh_token", refreshToken
                );
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception exception) {
                handleException(response, exception);
            }
        } else {
          throw new RuntimeException("Refresh token is missing");
        }

    }

    public void handleException(HttpServletResponse response, Exception exception) throws IOException {
        response.setHeader("error", exception.getMessage());
        response.setStatus(FORBIDDEN.value());

        Map<String, String> error = Map.of(
                "error_message", exception.getMessage()
        );
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

}
