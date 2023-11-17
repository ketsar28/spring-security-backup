package com.express.security.controller;

import com.express.security.entity.User;
import com.express.security.entity.VerificationToken;
import com.express.security.event.RegistrationCompleteEvent;
import com.express.security.model.CommonResponse;
import com.express.security.model.PasswordModel;
import com.express.security.model.UserModel;
import com.express.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public ResponseEntity<?> getUser(@RequestBody UserModel userModel, final HttpServletRequest requestUrl) {
        User user = userService.registerUser(userModel);

        RegistrationCompleteEvent event = new RegistrationCompleteEvent(user, applicationUrl(requestUrl));
        publisher.publishEvent(event);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.CREATED.toString())
                .data(user)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<?> verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid")) {
            CommonResponse<?> response = CommonResponse.builder()
                    .statusCode(HttpStatus.OK.toString())
                    .data("user has been verified successfully")
                    .build();

            return ResponseEntity.ok(response);
        }

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.toString())
                .data(result)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/resendVerifyToken")
    public ResponseEntity<?> generateNewToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
        VerificationToken verificationToken = userService.generateNewToken(oldToken);
        User user = verificationToken.getUser();
        resendVerificationToken(user, applicationUrl(request), verificationToken);
        CommonResponse<?> response = CommonResponse.builder()
                .data("verification link sent successfully")
                .statusCode(HttpStatus.CREATED.toString())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private void resendVerificationToken(User user, String applicationUrl, VerificationToken verificationToken) {
        // send mail to user
        String url = applicationUrl + "/auth/verifyRegistration?token=" + verificationToken.getToken();

        // verificationEmail()
        log.info("click the link to verify: {}", url);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if (Objects.nonNull(user)) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordForResetTokenOfUser(user, token);
            url = passwordResetTokenMail(user, applicationUrl(request), token);
        }
        CommonResponse<?> response = CommonResponse.builder()
                .data(url)
                .statusCode(HttpStatus.CREATED.toString())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody PasswordModel passwordModel) {
        User user = userService.findUserByEmail(passwordModel.getEmail());
        if(!userService.checkIfOldPasswordValid(user, passwordModel.getOldPassword())) {
            CommonResponse<?> response = CommonResponse.builder()
                    .data("Invalid Old Password")
                    .statusCode(HttpStatus.BAD_REQUEST.toString())
                    .build();

            return ResponseEntity.badRequest().body(response);
        }
        CommonResponse<?> response = CommonResponse.builder()
                .data("Password Changed Successfully")
                .statusCode(HttpStatus.CREATED.toString())
                .build();
        userService.changePassword(user, passwordModel.getNewPassword());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/saveResetPassword")
    public ResponseEntity<?> saveResetPassword(@RequestBody PasswordModel passwordModel,  @RequestParam(name = "token") String token) {
        String result = userService.validatePasswordResetToken(token);

        if (!result.equalsIgnoreCase("valid")) {
            CommonResponse<?> response = CommonResponse.builder()
                    .data("Invalid Token")
                    .statusCode(HttpStatus.BAD_REQUEST.toString())
                    .build();

            return ResponseEntity.badRequest().body(response);
        }
        Optional<User> user = userService.getUserByPasswordResetToken(token);

        if (user.isPresent()) {
            userService.changePassword(user.get(), passwordModel.getNewPassword());
            CommonResponse<?> response = CommonResponse.builder()
                    .data("Password Save Successfully")
                    .statusCode(HttpStatus.CREATED.toString())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            CommonResponse<?> response = CommonResponse.builder()
                    .data("Invalid Token")
                    .statusCode(HttpStatus.BAD_REQUEST.toString())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        // send mail to user
        String url = applicationUrl + "/auth/saveResetPassword?token=" + token;

        // verificationEmail()
        log.info("click the link to reset your password: {}", url);
        return url;
    }
}
