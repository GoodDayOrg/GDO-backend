package org.example.models;

import java.util.Map;

public class UserRole {

    public static final String ADMIN = "admin";
    public static final String USER = "user";
    int id;

    private static final Map<Integer, String> ROLES_MAP = Map.of(
            1, ADMIN,
            2, USER
    );

    public UserRole(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getRoleName() {
        return ROLES_MAP.get(getId());
    }

}
