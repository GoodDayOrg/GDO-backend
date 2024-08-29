package org.example.services;

import io.jsonwebtoken.Jwts;
import org.example.daos.AuthDao;
import org.example.exceptions.Entity;
import org.example.exceptions.InvalidException;
import org.example.models.LoginRequest;
import org.example.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.security.Key;
import java.sql.Date;
import java.sql.SQLException;

public class AuthService {

    private static final int MAGIC_NUM_288 = 28800000;
    private final AuthDao authDao;
    private final Key key;

    public AuthService(final AuthDao authDao, final Key key) {
        this.authDao = authDao;
        this.key = key;
    }

    public String login(final LoginRequest loginRequest)
            throws SQLException, InvalidException  {
        User user = authDao.getUser(loginRequest);

        if (user == null) {
            throw new InvalidException(Entity.USER, "User doesn't exist");
        }

        String requestPassword = loginRequest.getPassword();
        boolean isPasswordMatch = BCrypt.checkpw(requestPassword, user.getPassword());
        if (isPasswordMatch){
            return generateJwtToken(user);
        } else{
            throw new InvalidException(Entity.USER, "Invalid credentials");
        }
    }

    private String generateJwtToken(final User user) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()
                        + MAGIC_NUM_288))
                .claim("Role", user.getRoleId())
                .subject(user.getEmail())
                .issuer("DropwizardDemo")
                .signWith(key)
                .compact();
    }

}
