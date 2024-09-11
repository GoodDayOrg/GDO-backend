package org.example.validators;

import org.example.exceptions.Entity;
import org.example.exceptions.FileNeededException;
import org.example.exceptions.FileTooBigException;
import org.example.exceptions.InvalidFileTypeException;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class JobRoleImportValidator {
    private static final long MAX_FILE_SIZE_MB = 5L;
    private static final long MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024;

    public static void validateCsvFile(final byte[] fileBytes, final String fileDetails)
            throws FileNeededException, FileTooBigException, InvalidFileTypeException {

        if (fileBytes.length > MAX_FILE_SIZE_BYTES) {
            throw new FileTooBigException(Entity.FILE);
        }
        if (fileBytes.length == 0) {
            throw new FileNeededException(Entity.FILE);
        }
        if (!fileDetails.toLowerCase().endsWith(".csv")) {
            throw new InvalidFileTypeException(Entity.FILE);
        }
    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];

        int bytesRead;
        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        return buffer.toByteArray();
    }
}
