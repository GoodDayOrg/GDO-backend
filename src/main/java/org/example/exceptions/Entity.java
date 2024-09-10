package org.example.exceptions;

public enum Entity {

    JOB_ROLE("JobRole"),
    USER("User"),
    APPLICATION("Job applications");

    private final String entity;

    Entity(final String entity) {
        this.entity = entity;
    }

    public String getEntity() {
        return entity;
    }
}
