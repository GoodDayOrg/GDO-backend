package org.example.validators;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.example.daos.JobApplicationDao;
import org.example.daos.JobRoleDao;
import org.example.exceptions.AlreadyExistsException;
import org.example.exceptions.DoesNotExistException;
import org.example.exceptions.FileNeededException;
import org.example.exceptions.FileTooBigException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class JobApplicationValidatorTest {

    private static final int MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024; // 5MB in bytes

    JobRoleDao jobRoleDao = mock(JobRoleDao.class);
    JobApplicationDao jobApplicationDao = mock(JobApplicationDao.class);
    JobApplicationValidator jobApplicationValidator = new JobApplicationValidator(jobRoleDao, jobApplicationDao);


    int jobRoleId = 1;
    String userEmail = "user@example.com";

    @Test
    public void validateAndProduceByteArray_givenValidData_shouldReturnCorrectByteArray()
            throws FileTooBigException, IOException, DoesNotExistException, AlreadyExistsException,
            FileNeededException, SQLException {

        byte[] expectedByteContent = "valid content".getBytes();
        InputStream inputStream = new ByteArrayInputStream(expectedByteContent);

        when(jobRoleDao.existsOpenById(jobRoleId)).thenReturn(true);
        when(jobApplicationDao.existsByIdAndUserEmail(jobRoleId, userEmail)).thenReturn(false);

        byte[] result = jobApplicationValidator.validateAndProduceByteArray(jobRoleId, userEmail, inputStream);

        assertArrayEquals(expectedByteContent, result);
    }

    @Test
    public void validateAndProduceByteArray_givenFileTooBig_shouldThrowFileTooBigException() {

        byte[] largeFileContent = new byte[MAX_FILE_SIZE_BYTES + 1];
        InputStream inputStream = new ByteArrayInputStream(largeFileContent);

        FileTooBigException exception = assertThrows(
                FileTooBigException.class,
                () -> jobApplicationValidator.validateAndProduceByteArray(jobRoleId, userEmail, inputStream));
        assertEquals("File should not exceed 5MB", exception.getMessage());
    }

    @Test
    public void validateAndProduceByteArray_givenEmptyFile_shouldThrowFileNeededException() {
        byte[] emptyFileContent = new byte[0];
        InputStream inputStream = new ByteArrayInputStream(emptyFileContent);

        FileNeededException exception = assertThrows(
                FileNeededException.class,
                () -> jobApplicationValidator.validateAndProduceByteArray(jobRoleId, userEmail, inputStream));
        assertEquals("File is needed", exception.getMessage());
    }

    @Test
    public void validateAndProduceByteArray_whenJobRoleDoesNotExist_shouldThrowDoesNotExistException()
            throws SQLException {
        byte[] fileContent = "valid content".getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileContent);

        when(jobRoleDao.existsOpenById(jobRoleId)).thenReturn(false);

        DoesNotExistException exception = assertThrows(
                DoesNotExistException.class,
                () -> jobApplicationValidator.validateAndProduceByteArray(jobRoleId, userEmail, inputStream));

        assertEquals("Job Role does not exist", exception.getMessage());
    }

    @Test
    public void validateAndProduceByteArray_whenJobApplicationExists_shouldThrowAlreadyExistsException()
            throws SQLException {
        byte[] fileContent = "valid content".getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileContent);

        when(jobRoleDao.existsOpenById(jobRoleId)).thenReturn(true);
        when(jobApplicationDao.existsByIdAndUserEmail(jobRoleId, userEmail)).thenReturn(true);

        AlreadyExistsException exception = assertThrows(
                AlreadyExistsException.class,
                () -> jobApplicationValidator.validateAndProduceByteArray(jobRoleId, userEmail, inputStream));

        assertEquals("Job Application already exists", exception.getMessage());
    }

}
