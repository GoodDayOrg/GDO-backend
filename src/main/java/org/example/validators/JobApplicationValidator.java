package org.example.validators;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.example.daos.JobApplicationDao;
import org.example.daos.JobRoleDao;
import org.example.exceptions.AlreadyExistsException;
import org.example.exceptions.DoesNotExistException;
import org.example.exceptions.Entity;
import org.example.exceptions.FileNeededException;
import org.example.exceptions.FileTooBigException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class JobApplicationValidator {


    private static final long MAX_FILE_SIZE_MB = 5L;  // File size limit in MB
    private static final long MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024;

    public static byte[] validateAndProduceByteArray(final JobRoleDao jobRoleDao,
                                                   final JobApplicationDao jobApplicationDao,
                                                   final int jobRoleId,
                                                   final String userEmail,
                                                   final InputStream fileInputStream,
                                                   final ObjectMetadata metadata)
            throws FileTooBigException, SQLException, DoesNotExistException, IOException, AlreadyExistsException,
            FileNeededException {

        byte[] fileBytes = readInputStream(fileInputStream);
        System.out.println("file Bytes length: " + fileBytes.length);

        if (fileBytes.length > MAX_FILE_SIZE_BYTES){
            System.out.println("exceeded max size");
            throw new FileTooBigException();
        }
        if(fileBytes.length == 0 ){
            System.out.println("0 bytes found");
            throw new FileNeededException();
        }
        metadata.setContentLength(fileBytes.length);
        if (!jobRoleDao.existsOpenById(jobRoleId)){
            System.out.println("jobrole does not exist or is not open");
            throw new DoesNotExistException(Entity.JOB_ROLE);
        }
        if (jobApplicationDao.existsByIdAndUserEmail(jobRoleId, userEmail)){
            System.out.println("application already posted");
            throw new AlreadyExistsException(Entity.JOB_APPLICATION);
        }

        return fileBytes;
    }

    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];  // 4KB buffer size

        int bytesRead;
        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
            System.out.println("buffering data");
            buffer.write(data, 0, bytesRead);
        }

        return buffer.toByteArray();
    }
}
