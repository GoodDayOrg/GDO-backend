package org.example.models;

public enum ApplicationStatusId {
    IN_PROGRESS(1),
    HIRED(2),
    REJECTED(3);

    private final int statusId;

    ApplicationStatusId(final int statusId) {
        this.statusId = statusId;
    }

    public int getStatusId() {
        return statusId;
    }
}
