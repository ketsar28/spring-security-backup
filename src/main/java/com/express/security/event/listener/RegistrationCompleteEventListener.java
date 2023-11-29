package com.express.security.event.listener;

import com.express.security.entity.User;
import com.express.security.entity.UserDetailsApp;
import com.express.security.event.RegistrationCompleteEvent;
import com.express.security.service.UserService;
import com.express.security.service.impl.CustomUserDetailsImpl;
import com.express.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // create the verification token for the user with link
        User user = event.getUser();
        String token = jwtUtil.generateRegisterToken(user);

        userService.saveVerificationTokenForUser(user, token);

        // send mail to user
        String url = event.getApplicationUrl() + "/api/auth/verifyRegistration?token=" + token;

        // verificationEmail()
        log.info("click the link to verify: {}", url);
    }
}
