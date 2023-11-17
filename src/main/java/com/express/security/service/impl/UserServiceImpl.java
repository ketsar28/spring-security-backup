package com.express.security.service.impl;

import com.express.security.entity.PasswordResetToken;
import com.express.security.entity.User;
import com.express.security.entity.VerificationToken;
import com.express.security.model.UserModel;
import com.express.security.repository.PasswordResetTokenRepository;
import com.express.security.repository.UserRepository;
import com.express.security.repository.VerificationTokenRepository;
import com.express.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public User registerUser(UserModel userModel) {

        if(userRepository.existsByEmail(userModel.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");
        }

        User user = User.builder()
                .email(userModel.getEmail())
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .role("USER")
                .password(passwordEncoder.encode(userModel.getPassword()))
                .enable(false)
                .build();

        userRepository.save(user);

        return user;
    }

    @Override
    public void saveVerificationTokenForUser(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "token not found"));

        if(Objects.isNull(verificationToken)) {
            return "invalid";
        }

        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();

        if(verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }

        user.setEnable(true);
        verificationTokenRepository.save(verificationToken);
        return "valid";
    }

    @Override
    public VerificationToken generateNewToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "token not found"));

        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    }

    @Override
    public void createPasswordForResetTokenOfUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if(Objects.isNull(passwordResetToken)) {
            return "invalid";
        }

        Calendar calendar = Calendar.getInstance();

        if(passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }

        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfOldPasswordValid(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

}
