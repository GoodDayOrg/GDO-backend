package org.example.validators;

import org.example.exceptions.FileNeededException;
import org.example.exceptions.FileTooBigException;
import org.example.exceptions.InvalidFileTypeException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JobRoleImportValidatorTest {

    @Test
    public void getJobRolesFromCsv_whenFileIsTooBig_ShouldReturnFileTooBigException() {
        byte[] largeFileContent = new byte[(int)JobRoleImportValidator.MAX_FILE_SIZE_BYTES + 1];
        InputStream largeFileInputStream = new ByteArrayInputStream(largeFileContent);

        assertThrows(FileTooBigException.class, () -> JobRoleImportValidator.validateCsvFile(
                largeFileContent, "largeFile.csv"));
    }

    @Test
    public void getJobRolesFromCsv_whenFileIsEmpty_shouldReturnFileNeededException() {
        byte[] emptyFileContent = new byte[0];
        assertThrows(FileNeededException.class, () -> JobRoleImportValidator.validateCsvFile(
                emptyFileContent, "emptyFile.csv"));
    }

    @Test
    public void getJobRolesFromCsv_whenFileIsNotCsv_shouldReturnInvalidFileTypeException() {
        byte[] fileContent = new byte[1];
        String wrongFileExtension = "wrongFile.txt";
        assertThrows(InvalidFileTypeException.class, () -> JobRoleImportValidator.validateCsvFile(
                fileContent, wrongFileExtension));
    }
}
