package org.example.exceptions;

public class FileNeededException extends Throwable {

    private static final String MESSAGE = " is needed";

    public FileNeededException(final Entity entity) {
        super(entity.getEntity() + MESSAGE);
    }
}
