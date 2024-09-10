package org.example.models;

import javax.security.auth.Subject;
import java.security.Principal;

public class JwtToken implements Principal {

    private UserRole userRole;
    private String userEmail;

    public JwtToken(final UserRole userRole, final String userEmail) {
        setUserEmail(userEmail);
        setUserRole(userRole);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean implies(final Subject subject) {
        return Principal.super.implies(subject);
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(final UserRole userRole) {
        this.userRole = userRole;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(final String userEmail) {
        this.userEmail = userEmail;
    }
}
