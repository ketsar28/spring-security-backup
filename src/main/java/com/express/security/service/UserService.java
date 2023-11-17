package com.express.security.service;

import com.express.security.entity.User;
import com.express.security.entity.VerificationToken;
import com.express.security.model.UserModel;

import java.util.Optional;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(User user, String token);

    String validateVerificationToken(String token);

    VerificationToken generateNewToken(String oldToken);

    User findUserByEmail(String email);

    void createPasswordForResetTokenOfUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);

    boolean checkIfOldPasswordValid(User user, String oldPassword);
}
