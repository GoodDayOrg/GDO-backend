package org.example.exceptions;

public class InvalidFileTypeException extends Throwable {
    private static final String MESSAGE = " has wrong type";
    public InvalidFileTypeException(final Entity entity) {
        super(entity.getEntity() + MESSAGE);
    }
}
