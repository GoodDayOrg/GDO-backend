package org.example.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import org.example.daos.AuthDao;
import org.example.exceptions.InvalidException;
import org.example.models.LoginRequest;
import org.example.models.User;
import org.example.validators.AuthValidator;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;

public class AuthServiceTest {

    AuthDao authDao = Mockito.mock(AuthDao.class);
    AuthValidator authValidator = Mockito.mock(AuthValidator.class);
    AuthService authService = new AuthService(authDao, authValidator);

    @Test
    public void login_GivenValidLoginRequest_WhenDaoReturnsNullUser_ShouldThrowInvalidException() throws SQLException {
        when(authDao.getUser(any())).thenReturn(null);
        assertThrows(InvalidException.class, () -> authService.login(new LoginRequest("test@example.com", "admin")));
    }

    @Test
    public void login_GivenPasswordDoesNotMatch_ReturnInvalidException() throws SQLException, InvalidException {
        LoginRequest loginRequest = new LoginRequest("username@example.com", "password");
        String hashedpassword = BCrypt.hashpw("correctpassword", BCrypt.gensalt());

        User user = new User("username@example.com", hashedpassword, 1);
        when(authDao.getUser(loginRequest)).thenReturn(user);

        assertThrows(InvalidException.class, () -> authService.login(loginRequest));
    }

    @Test
    public void login_shouldReturnGeneratedJwtToken_whenPasswordMatches() throws SQLException, InvalidException {
        String plainPassword = "password";
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        LoginRequest loginRequest = new LoginRequest("email@example.com", plainPassword);

        User user = new User("email@example.com", hashedPassword, 1); // Użytkownik z zaszyfrowanym hasłem

        when(authDao.getUser(loginRequest)).thenReturn(user);
        when(authValidator.validateEmail(loginRequest.getEmail())).thenReturn(true);
        String token = authService.login(loginRequest);

        assertNotNull(token);
        assertTrue(BCrypt.checkpw(plainPassword, hashedPassword));
    }
}
