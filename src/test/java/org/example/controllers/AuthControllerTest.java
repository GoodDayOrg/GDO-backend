package org.example.controllers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.example.daos.AuthDao;
import org.example.exceptions.InvalidException;
import org.example.models.LoginRequest;
import org.example.models.User;
import org.example.services.AuthService;
import org.example.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    private final String email = "admin";
    private final String plainTextPassword = "admin";
    @Mock
    private AuthDao authDao;
    @InjectMocks
    private AuthService authservice;
    private final AuthController authController =
            new AuthController(authservice);
    private User testUser;
    private LoginRequest loginRequest =
            new LoginRequest(email, plainTextPassword);
    private String hashedPassword;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        hashedPassword = BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
        testUser = new User(email, hashedPassword, 1);

        secretKey = JwtUtils.getSecretKey();
    }


    @Test
    void authenticate_ShouldReturnValidJwtToken_WhenCredentialsAreValid()
            throws SQLException, InvalidException {
        // Arrange
        when(authDao.getUser(loginRequest)).thenReturn(testUser);

        // Act
        String token = authservice.login(loginRequest);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Parse and validate the token
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(email, claims.getSubject()); // Verify the email
        assertTrue(claims.containsKey("email")); // Verify the email claim
        assertEquals(email, claims.get("email")); // Verify the email value
    }


}
