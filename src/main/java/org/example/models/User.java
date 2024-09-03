package org.example.models;

public class User {
    private final String email;
    private final String password;
    private final int roleId;

    public User(final String email,
                final String password,
                final int roleId) {
        this.email = email;
        this.password = password;
        this.roleId = roleId;
    }

    public String getEmail() {
        return email;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getPassword() {
        return password;
    }
}


