package org.example.services;

import java.sql.SQLException;
import org.example.daos.AuthDao;
import org.example.exceptions.Entity;
import org.example.exceptions.InvalidException;
import org.example.models.LoginRequest;
import org.example.models.User;
import org.example.utils.JwtUtils;
import org.example.validators.AuthValidator;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private static final int MAGIC_NUM_288 = 28800000;
    private final AuthDao authDao;
    private AuthValidator authValidator;

    public AuthService(final AuthDao authDao, final AuthValidator authValidator) {
        this.authDao = authDao;
        this.authValidator = authValidator;
    }

    public String login(final LoginRequest loginRequest) throws SQLException, InvalidException {
        if (!authValidator.validateEmail(loginRequest.getEmail())) {
            throw new InvalidException(Entity.USER, "Invalid email");
        }
        User user = authDao.getUser(loginRequest);

        if (user == null) {
            throw new InvalidException(Entity.USER, "User doesn't exist");
        }

        String requestPassword = loginRequest.getPassword();
        boolean isPasswordMatch = BCrypt.checkpw(requestPassword, user.getPassword());
        if (isPasswordMatch) {
            return JwtUtils.generateToken(user.getEmail(), user.getRoleId());
        } else {
            throw new InvalidException(Entity.USER, "Invalid credentials");
        }
    }
}
