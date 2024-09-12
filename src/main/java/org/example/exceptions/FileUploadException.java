package org.example.exceptions;

public class FileUploadException extends Throwable {

    public FileUploadException() {
        super("Could not upload file. Please try again in 5 minutes");
    }
}
