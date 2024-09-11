package org.example.exceptions;

public class FileTooBigException extends Throwable{
    private static final String MESSAGE = "File should not exceed 5MB";
    public FileTooBigException(final Entity entity) {
        super(entity.getEntity() +  MESSAGE);
    }}