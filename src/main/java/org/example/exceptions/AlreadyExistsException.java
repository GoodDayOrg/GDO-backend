package org.example.exceptions;

public class AlreadyExistsException extends Throwable {
    public AlreadyExistsException(final Entity entity) {
        super(entity.getEntity() + " already exists");
    }
}
