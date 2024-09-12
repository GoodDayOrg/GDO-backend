package org.example.exceptions;

public enum Entity {
    JOB_ROLE("Job Role"),
    USER("User"),
    JOB_APPLICATION("Job Application");

    private final String entity;

    Entity(final String entity) {
        this.entity = entity;
    }

    public String getEntity() {
        return entity;
    }
}
