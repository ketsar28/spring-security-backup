package com.express.security.controller;

import com.express.security.dto.CommonResponse;
import com.express.security.dto.PasswordRequest;
import com.express.security.dto.UserRequest;
import com.express.security.entity.User;
import com.express.security.service.UserService;
import com.express.security.service.impl.CustomUserDetailsImpl;
import com.express.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.lang.invoke.VarHandle.AccessMode.GET;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

//    @PostMapping
//    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
//    public ResponseEntity<?> registerUser(@RequestBody UserRequest request){
//        CommonResponse<?> response = CommonResponse.builder()
//                .data(userService.registerUser(request))
//                .statusCode(CREATED)
//                .build();
//        return ResponseEntity.status(CREATED).body(response);
//    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_ADMIN')")
    public ResponseEntity<?> findAllUsers() {
        CommonResponse<?> response = CommonResponse.builder()
                .data(userService.findAllUsers())
                .statusCode(OK)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> findUserByUsername(@PathVariable String username) {
        CommonResponse<?> response = CommonResponse.builder()
                .data(userService.findUserByUsername(username))
                .statusCode(OK)
                .build();
        return ResponseEntity.ok(response);
    }

    //    @PreAuthorize("hasRole('ROLE_USER') and @userFilter.checkUser(authentication, #user?.getUsername())")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/credentials/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordRequest passwordRequest, HttpServletRequest request) {
        User user = userService.findUserByEmail(passwordRequest.getEmail());
        String url = "";

        if (Objects.nonNull(user)) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordForResetTokenOfUser(user, token);
            url = passwordResetTokenMail(user, applicationUrl(request), token);
        }
        CommonResponse<?> response = CommonResponse.builder()
                .data(url)
                .statusCode(CREATED)
                .build();

        return ResponseEntity.status(CREATED).body(response);
    }

    private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        // send mail to user
        String url = applicationUrl + "/api/users/credentials/saveResetPassword?token=" + token;

        // verificationEmail()
        log.info("click the link to reset your password: {}", url);
        return url;
    }

    private String applicationUrl(HttpServletRequest requestUrl) {
        return "http://" + requestUrl.getServerName() + ":" + requestUrl.getServerPort() + requestUrl.getContextPath();
    }

    //    @PreAuthorize("hasRole('ROLE_USER') and @userFilter.checkUser(authentication, #user?.getUsername())")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/credentials/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody PasswordRequest passwordRequest) {
        User user = userService.findUserByEmail(passwordRequest.getEmail());
        if (!userService.checkIfOldPasswordValid(user, passwordRequest.getOldPassword())) {
            CommonResponse<?> response = CommonResponse.builder()
                    .data("Invalid Old Password")
                    .statusCode(UNAUTHORIZED)
                    .build();

            return ResponseEntity.status(UNAUTHORIZED).body(response);
        }
        CommonResponse<?> response = CommonResponse.builder()
                .data("Password Changed Successfully")
                .statusCode(CREATED)
                .build();
        userService.changePassword(user, passwordRequest.getNewPassword());

        return ResponseEntity.status(CREATED).body(response);

    }

    //    @PreAuthorize("hasRole('ROLE_USER') and @userFilter.checkUser(authentication, #user?.getUsername())")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/credentials/saveResetPassword")
    public ResponseEntity<?> saveResetPassword(@RequestBody PasswordRequest passwordRequest, @RequestParam(name = "token") String token) {
        String result = userService.validatePasswordResetToken(token);

        if (!result.equalsIgnoreCase("Valid")) {
            CommonResponse<?> response = CommonResponse.builder()
                    .data("Invalid Token")
                    .statusCode(BAD_REQUEST)
                    .build();

            return ResponseEntity.badRequest().body(response);
        }
        Optional<User> user = userService.getUserByPasswordResetToken(token);

        if (user.isPresent()) {
            userService.changePassword(user.get(), passwordRequest.getNewPassword());
            CommonResponse<?> response = CommonResponse.builder()
                    .data("Password Save Successfully")
                    .statusCode(CREATED)
                    .build();
            return ResponseEntity.status(CREATED).body(response);
        } else {
            CommonResponse<?> response = CommonResponse.builder()
                    .data("Invalid Token")
                    .statusCode(INTERNAL_SERVER_ERROR)
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
