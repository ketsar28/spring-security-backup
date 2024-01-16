package com.express.security.controller;

import com.express.security.dto.AuthenticationRequest;
import com.express.security.dto.CommonResponse;
import com.express.security.dto.UserRequest;
import com.express.security.entity.User;
import com.express.security.entity.VerificationToken;
import com.express.security.event.RegistrationCompleteEvent;
import com.express.security.service.UserService;
import com.express.security.service.impl.CustomUserDetailsImpl;
import com.express.security.util.JwtUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication & Authorization API")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsImpl customUserDetails;

    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest request, HttpServletRequest requestUrl) {
        User user = userService.registerUser(request);
        RegistrationCompleteEvent event = new RegistrationCompleteEvent(user, applicationUrl(requestUrl));

        publisher.publishEvent(event);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.CREATED)
                .data(user)
                .build();

        return ResponseEntity.status(CREATED).body(response);
    }

    private String applicationUrl(HttpServletRequest requestUrl) {
        return "http://" + requestUrl.getServerName() + ":" + requestUrl.getServerPort() + requestUrl.getContextPath();
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<?> verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid")) {
            CommonResponse<?> response = CommonResponse.builder()
                    .statusCode(OK)
                    .data("user has been verified successfully")
                    .build();

            return ResponseEntity.ok(response);
        }

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(BAD_REQUEST)
                .data(result)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/resendVerifyToken")
    public ResponseEntity<?> generateNewToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
        VerificationToken verificationToken = userService.generateNewToken(oldToken, request);

        User user = verificationToken.getUser();
        resendVerificationToken(user, applicationUrl(request), verificationToken);

        CommonResponse<?> response = CommonResponse.builder()
                .data("verification link sent successfully")
                .statusCode(CREATED)
                .build();
        return ResponseEntity.status(CREATED).body(response);
    }

    private void resendVerificationToken(User user, String applicationUrl, VerificationToken verificationToken) {
        // send mail to user
        String url = applicationUrl + "/api/auth/verifyRegistration?token=" + verificationToken.getToken();

        // verificationEmail()
        log.info("click the link to verify: {}", url);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        log.info("Login request: {}", request);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails userDetails = customUserDetails.loadUserByUsername(request.getUsername());
        if (Objects.nonNull(userDetails)) {
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
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
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
