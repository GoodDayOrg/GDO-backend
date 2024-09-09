package org.example.exceptions;

public class FileNeededException extends Throwable{
    private static final String MESSAGE = "File is needed";
    public FileNeededException() {
        super(MESSAGE);
    }
}
