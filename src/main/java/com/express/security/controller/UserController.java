package com.express.security.controller;

import com.express.security.dto.CommonResponse;
import com.express.security.entity.User;
import com.express.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static java.lang.invoke.VarHandle.AccessMode.GET;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<?> saveUser(@RequestBody User request){
        CommonResponse<?> response = CommonResponse.builder()
                .data(userService.saveUser(request))
                .statusCode(CREATED)
                .build();
        return ResponseEntity.status(CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    public ResponseEntity<?> findAllUsers() {
        CommonResponse<?> response = CommonResponse.builder()
                .data(userService.findAllUsers())
                .statusCode(OK)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('ROLE_SUPERVISOR','ROLE_ADMIN')")
    public ResponseEntity<?> findUserByUsername(@PathVariable String username) {
        CommonResponse<?> response = CommonResponse.builder()
                .data(userService.findUserByUsername(username))
                .statusCode(OK)
                .build();
        return ResponseEntity.ok(response);
    }
}
