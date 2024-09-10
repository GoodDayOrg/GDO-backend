package org.example.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.example.models.JwtToken;
import org.example.models.UserRole;

import java.security.Key;
import java.util.Optional;

public class JwtAuthenticator implements Authenticator<String, JwtToken> {
    private final Key key;

    public JwtAuthenticator(final Key key) {
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

    @Override
    public Optional<JwtToken> authenticate(final String token)
            throws AuthenticationException {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Integer roleId = claims.get("role_id", Integer.class);
            String userEmail = claims.get("email", String.class);

            JwtToken jwtToken = new JwtToken(new UserRole(roleId), userEmail);

            return Optional.of(jwtToken);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
