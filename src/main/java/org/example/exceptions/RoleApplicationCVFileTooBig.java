package org.example.exceptions;

public class RoleApplicationCVFileTooBig extends Throwable {

    private static final String BASE_MESSAGE = "File should not exceed 10MB but was: ";

    public RoleApplicationCVFileTooBig(final String message) {
        super(BASE_MESSAGE + message);
    }
}
