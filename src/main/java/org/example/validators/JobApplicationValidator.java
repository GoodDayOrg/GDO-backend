package org.example.validators;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import org.example.daos.JobApplicationDao;
import org.example.daos.JobRoleDao;
import org.example.exceptions.AlreadyExistsException;
import org.example.exceptions.DoesNotExistException;
import org.example.exceptions.Entity;
import org.example.exceptions.FileNeededException;
import org.example.exceptions.FileTooBigException;

public class JobApplicationValidator {

    private static final long MAX_FILE_SIZE_MB = 5L; // File size limit in MB
    private static final long MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024;
    private static final int FILE_READING_BUFFER_SIZE = 4096;

    private final JobRoleDao jobRoleDao;
    private final JobApplicationDao jobApplicationDao;

    public JobApplicationValidator(final JobRoleDao jobRoleDao, final JobApplicationDao jobApplicationDao) {
        this.jobRoleDao = jobRoleDao;
        this.jobApplicationDao = jobApplicationDao;
    }

    public byte[] validateAndProduceByteArray(
            final int jobRoleId, final String userEmail, final InputStream fileInputStream)
            throws FileTooBigException, SQLException, DoesNotExistException, IOException, AlreadyExistsException,
                    FileNeededException {

        byte[] fileBytes = readInputStream(fileInputStream);

        if (fileBytes.length > MAX_FILE_SIZE_BYTES) {
            throw new FileTooBigException(Entity.FILE);
        }
        if (fileBytes.length == 0) {
            throw new FileNeededException(Entity.FILE);
        }
        if (!jobRoleDao.existsOpenById(jobRoleId)) {
            throw new DoesNotExistException(Entity.JOB_ROLE);
        }
        if (jobApplicationDao.existsByIdAndUserEmail(jobRoleId, userEmail)) {
            throw new AlreadyExistsException(Entity.JOB_APPLICATION);
        }

        return fileBytes;
    }

    private byte[] readInputStream(final InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        byte[] data = new byte[FILE_READING_BUFFER_SIZE];

        int bytesRead;
        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        return buffer.toByteArray();
    }
}
