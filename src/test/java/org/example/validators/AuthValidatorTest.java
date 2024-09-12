package org.example.validators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AuthValidatorTest {
    private AuthValidator authValidator = new AuthValidator();

    @Test
    void testValidateEmail_withValidEmail_shouldReturnTrue() {
        String email = "user@example.com";
        assertTrue(authValidator.validateEmail(email), "Valid email should return True");
    }

    @Test
    void testValidateEmail_withInvalidEmail_shouldReturnFalse() {
        String email = "abcd";
        assertFalse(authValidator.validateEmail(email), "Invalid email should return False");
    }

    @Test
    void testValidateEmail_withNullEmail_shouldReturnFalse() {
        assertFalse(authValidator.validateEmail(null), "Null email should return False");
    }

    @Test
    void testValidateEmail_withEmptyEmail_shouldReturnFalse() {
        String email = "";
        assertFalse(authValidator.validateEmail(email), "Empty email should return False");
    }

    @Test
    void testValidateEmail_emailWithoutDomain_shouldReturnFalse() {
        String email = "user@";
        assertFalse(authValidator.validateEmail(email), "Email without domain should return False");
    }

    @Test
    void testValidateEmail_emailWithoutPrefix_shouldReturnFalse() {
        String email = "@example.com";
        assertFalse(authValidator.validateEmail(email), "Email without prefix should return False");
    }
}
