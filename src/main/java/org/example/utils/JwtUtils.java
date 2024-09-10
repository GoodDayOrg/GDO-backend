package org.example.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class JwtUtils {
    private static final SecretKey SECRET_KEY =
            Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 86400000;

    private JwtUtils() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }

    public static String generateToken(final String email, final int roleId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("role_id", roleId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static SecretKey getSecretKey() {
        return SECRET_KEY;
    }

    public static boolean validateToken(final String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException | SecurityException e) {
            throw new IllegalArgumentException(
                    "Invalid JWT signature: " + e.getMessage(), e);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new IllegalArgumentException(
                    "JWT token is expired: " + e.getMessage(), e);
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            throw new IllegalArgumentException(
                    "Invalid JWT token: " + e.getMessage(), e);
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            throw new IllegalArgumentException(
                    "JWT token is unsupported: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "JWT claims string is empty or null: " + e.getMessage(), e);
        }
    }
}
