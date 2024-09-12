package org.example.services;

import static org.example.utils.Utils.assertEqualLists;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.example.daos.JobApplicationDao;
import org.example.daos.JobRoleDao;
import org.example.exceptions.*;
import org.example.exceptions.AlreadyExistsException;
import org.example.exceptions.DoesNotExistException;
import org.example.exceptions.Entity;
import org.example.exceptions.FileNeededException;
import org.example.exceptions.FileTooBigException;
import org.example.exceptions.FileUploadException;
import org.example.exceptions.ResultSetException;
import org.example.mappers.JobRoleMapper;
import org.example.models.*;
import org.example.models.JobRole;
import org.example.models.JobRoleApplication;
import org.example.models.JobRoleDetails;
import org.example.models.JobRoleFilteredRequest;
import org.example.models.JobRoleResponse;
import org.example.validators.JobApplicationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class JobRoleServiceTest {
    List<JobRole> jobRoles;
    JobRoleDao jobRoleDao = Mockito.mock(JobRoleDao.class);
    JobApplicationDao jobApplicationDao = Mockito.mock(JobApplicationDao.class);
    JobApplicationValidator jobApplicationValidator = Mockito.mock(JobApplicationValidator.class);
    JobRoleService jobRoleService = new JobRoleService(jobRoleDao, jobApplicationDao, jobApplicationValidator);

    @BeforeEach
    public void jobRolesListClean() {
        jobRoles = new ArrayList<>();
    }

    @Nested
    class GetAllJobRolesTests {

        @Test
        public void getAllJobRoles_shouldReturnListOfJobRolesResponse()
                throws SQLException, ResultSetException, DoesNotExistException {
            jobRoles.add(new JobRole(
                    3, "test", "Belfast", "testCapability", "testBand", Date.valueOf("2000-10-10"), "open"));
            jobRoles.add(new JobRole(
                    2, "test2", "Belfast", "testCapability2", "testBand2", Date.valueOf("2000-10-11"), "closed"));
            when(jobRoleDao.getAllJobRoles()).thenReturn(jobRoles);

            List<JobRoleResponse> expected = new ArrayList<>();
            expected.add(new JobRoleResponse(
                    3, "test", "Belfast", "testCapability", "testBand", Date.valueOf("2000-10-10"), "open"));

            List<JobRoleResponse> result = jobRoleService.getAllJobRoles();

            // Filter the result to include only JobRoleResponse with status "open"
            List<JobRoleResponse> filteredResult = result.stream()
                    .filter(jobRole -> "open".equals(jobRole.getStatusName()))
                    .collect(Collectors.toList());

            // Check if the filtered result is non-Null
            assertTrue(filteredResult.stream().allMatch(Objects::nonNull));

            // Check if the filtered result matches the expected list
            assertEquals(expected, filteredResult);
        }

        @Test
        public void getAllJobRoles_WhenDaoReturnsNull_ExpectDoesNotExistExceptionToBeThrown()
                throws DoesNotExistException, SQLException, ResultSetException {
            when(jobRoleDao.getAllJobRoles()).thenReturn(new ArrayList<JobRole>());
            assertThrows(DoesNotExistException.class, () -> jobRoleService.getAllJobRoles());
        }

        @Test
        public void getAllJobRoles_WhenDaoThrowsSQLException_ExpectSQLExceptionToBeThrown()
                throws SQLException, ResultSetException {
            when(jobRoleDao.getAllJobRoles()).thenThrow(SQLException.class);
            assertThrows(SQLException.class, () -> jobRoleService.getAllJobRoles());
        }

        @Test
        public void getJobRoleById_shouldReturnJobRoleDetails() throws SQLException, DoesNotExistException {
            JobRoleDetails expectedResult = new JobRoleDetails(
                    "test",
                    "Belfast",
                    "testCapability",
                    "testBand",
                    Date.valueOf("2000-10-11"),
                    "open",
                    "testDescription",
                    "testResponsibilities",
                    "http://url.com",
                    2);
            Mockito.when(jobRoleDao.getJobRoleById(1)).thenReturn(expectedResult);
            JobRoleDetails result = jobRoleService.getJobRoleById(1);
            assertEquals(expectedResult, result);
        }

        @Test
        public void getJobRoleById_whenDaoThrowsSQLException_ExpectSQLExceptionToBeThrown() throws SQLException {
            Mockito.when(jobRoleDao.getJobRoleById(1)).thenThrow(SQLException.class);
            assertThrows(SQLException.class, () -> jobRoleService.getJobRoleById(1));
        }

        @Test
        public void getJobRoleById_whenDaoReturnsNull_ExpectDoesNotExistExceptionToBeThrown() throws SQLException {
            Mockito.when(jobRoleDao.getJobRoleById(1)).thenReturn(null);
            assertThrows(DoesNotExistException.class, () -> jobRoleService.getJobRoleById(1));
        }

        @Test
        public void testGetFilteredJobRolesEmptyList() throws SQLException, ResultSetException {
            when(jobRoleDao.getFilteredJobRoles(any())).thenReturn(Collections.emptyList());
            assertThrows(
                    DoesNotExistException.class,
                    () -> jobRoleService.getFilteredJobRoles(new JobRoleFilteredRequest()));
        }

        @Test
        public void getFilteredJobRoles_WhenDaoThrowsSQLException_ExpectSQLExceptionToBeThrown()
                throws ResultSetException, SQLException {
            when(jobRoleDao.getFilteredJobRoles(any())).thenThrow(SQLException.class);
            assertThrows(SQLException.class, () -> jobRoleService.getFilteredJobRoles(any()));
        }

        @Test
        public void getFilteredJobRoles_shouldReturnListOfJobRolesResponse()
                throws SQLException, ResultSetException, DoesNotExistException {
            JobRoleFilteredRequest jobRoleFilteredRequest = new JobRoleFilteredRequest();
            jobRoles.add(new JobRole(
                    3, "test", "Belfast", "testCapability", "testBand", Date.valueOf("2000-10-10"), "open"));
            when(jobRoleDao.getFilteredJobRoles(jobRoleFilteredRequest)).thenReturn(jobRoles);

            List<JobRoleResponse> expected = new ArrayList<>();
            expected.add(new JobRoleResponse(
                    3, "test", "Belfast", "testCapability", "testBand", Date.valueOf("2000-10-10"), "open"));
            var result = jobRoleService.getFilteredJobRoles(jobRoleFilteredRequest);
            assertEqualLists(expected, result);
        }
    }

    @Nested
    class ApplyForRoleTests {

        int jobRoleId = 1;
        String userEmail = "user@example.com";
        InputStream inputStream = new ByteArrayInputStream("mockContent".getBytes());

        @Test
        public void applyForRole_givenValidInput_shouldCallJobApplicationDaoWithCorrectArguments()
                throws DoesNotExistException, FileTooBigException, AlreadyExistsException, SQLException, IOException,
                        FileNeededException, FileUploadException {

            ObjectMetadata expectedMetadata = new ObjectMetadata();
            expectedMetadata.addUserMetadata("jobRoleId", String.valueOf(1));
            expectedMetadata.addUserMetadata("userEmail", "user@example.com");
            expectedMetadata.setContentType("application/pdf");
            byte[] byteArray = "mockContent".getBytes();
            expectedMetadata.setContentLength(byteArray.length);

            when(jobRoleDao.existsOpenById(jobRoleId)).thenReturn(true);
            when(jobApplicationDao.existsByIdAndUserEmail(jobRoleId, userEmail)).thenReturn(false);
            when(jobApplicationValidator.validateAndProduceByteArray(jobRoleId, userEmail, inputStream))
                    .thenReturn(byteArray);

            jobRoleService.applyForRole(jobRoleId, userEmail, inputStream);

            ArgumentCaptor<ObjectMetadata> metadataCaptor = ArgumentCaptor.forClass(ObjectMetadata.class);
            verify(jobApplicationDao)
                    .applyForRole(eq(jobRoleId), eq(userEmail), eq(byteArray), metadataCaptor.capture());

            ObjectMetadata actualMetadata = metadataCaptor.getValue();

            assertEquals(
                    expectedMetadata.getUserMetadata().get("jobRoleId"),
                    actualMetadata.getUserMetadata().get("jobRoleId"));
            assertEquals(
                    expectedMetadata.getUserMetadata().get("userEmail"),
                    actualMetadata.getUserMetadata().get("userEmail"));
            assertEquals(expectedMetadata.getContentType(), actualMetadata.getContentType());
            assertEquals(expectedMetadata.getContentLength(), actualMetadata.getContentLength());
        }

        @Test
        public void applyForRole_givenValidInput_whenValidatorThrowsFileTooBigException_shouldThrowFileTooBigException()
                throws DoesNotExistException, FileTooBigException, AlreadyExistsException, SQLException, IOException,
                        FileNeededException {
            when(jobApplicationValidator.validateAndProduceByteArray(jobRoleId, userEmail, inputStream))
                    .thenThrow(new FileTooBigException(Entity.FILE));

            assertThrows(
                    FileTooBigException.class, () -> jobRoleService.applyForRole(jobRoleId, userEmail, inputStream));
        }

        @Test
        public void applyForRole_givenValidInput_whenValidatorThrowsFileNeededException_shouldThrowFileNeededException()
                throws DoesNotExistException, FileTooBigException, AlreadyExistsException, SQLException, IOException,
                        FileNeededException {
            when(jobApplicationValidator.validateAndProduceByteArray(jobRoleId, userEmail, inputStream))
                    .thenThrow(new FileNeededException(Entity.FILE));

            assertThrows(
                    FileNeededException.class, () -> jobRoleService.applyForRole(jobRoleId, userEmail, inputStream));
        }

        @Test
        public void
                applyForRole_givenValidInput_whenValidatorThrowsDoesNotExistException_shouldThrowDoesNotExistException()
                        throws DoesNotExistException, FileTooBigException, AlreadyExistsException, SQLException,
                                IOException, FileNeededException {
            when(jobApplicationValidator.validateAndProduceByteArray(jobRoleId, userEmail, inputStream))
                    .thenThrow(new DoesNotExistException(Entity.JOB_ROLE));

            assertThrows(
                    DoesNotExistException.class, () -> jobRoleService.applyForRole(jobRoleId, userEmail, inputStream));
        }

        @Test
        public void
                applyForRole_givenValidInput_whenValidatorThrowsAlreadyExistsException_shouldThrowAlreadyExistsException()
                        throws DoesNotExistException, FileTooBigException, AlreadyExistsException, SQLException,
                                IOException, FileNeededException {
            when(jobApplicationValidator.validateAndProduceByteArray(jobRoleId, userEmail, inputStream))
                    .thenThrow(new AlreadyExistsException(Entity.JOB_APPLICATION));

            assertThrows(
                    AlreadyExistsException.class, () -> jobRoleService.applyForRole(jobRoleId, userEmail, inputStream));
        }
    }

    @Test
    public void getAllJobRoles_shouldReturnListOfJobRolesResponse()
            throws SQLException, ResultSetException, DoesNotExistException {

        jobRoles.add(
                new JobRole(3, "test", "Belfast", "testCapability", "testBand", Date.valueOf("2000-10-10"), "open"));
        jobRoles.add(new JobRole(
                2, "test2", "Belfast", "testCapability2", "testBand2", Date.valueOf("2000-10-11"), "closed"));
        when(jobRoleDao.getAllJobRoles()).thenReturn(jobRoles);

        List<JobRoleResponse> expected = new ArrayList<>();
        expected.add(new JobRoleResponse(
                3, "test", "Belfast", "testCapability", "testBand", Date.valueOf("2000-10-10"), "open"));
        List<JobRoleResponse> result = jobRoleService.getAllJobRoles();
        List<JobRoleResponse> filteredResult = result.stream()
                .filter(jobRole -> "open".equals(jobRole.getStatusName()))
                .collect(Collectors.toList());

        assertTrue(filteredResult.stream().allMatch(Objects::nonNull));
        assertEquals(expected, filteredResult);
    }

    @Test
    public void getAllJobRoles_WhenDaoReturnsNull_ExpectDoesNotExistExceptionToBeThrown()
            throws DoesNotExistException, SQLException, ResultSetException {
        when(jobRoleDao.getAllJobRoles()).thenReturn(new ArrayList<JobRole>());
        assertThrows(DoesNotExistException.class, () -> jobRoleService.getAllJobRoles());
    }

    @Test
    public void getAllJobRoles_WhenDaoThrowsSQLException_ExpectSQLExceptionToBeThrown()
            throws SQLException, ResultSetException {
        when(jobRoleDao.getAllJobRoles()).thenThrow(SQLException.class);
        assertThrows(SQLException.class, () -> jobRoleService.getAllJobRoles());
    }

    @Test
    public void getJobRoleById_shouldReturnJobRoleDetails() throws SQLException, DoesNotExistException {
        JobRoleDetails expectedResult = new JobRoleDetails(
                "test",
                "Belfast",
                "testCapability",
                "testBand",
                Date.valueOf("2000-10-11"),
                "open",
                "testDescription",
                "testResponsibilities",
                "http://url.com",
                2);
        Mockito.when(jobRoleDao.getJobRoleById(1)).thenReturn(expectedResult);
        JobRoleDetails result = jobRoleService.getJobRoleById(1);
        assertEquals(expectedResult, result);
    }

    @Test
    public void getJobRoleById_whenDaoThrowsSQLException_ExpectSQLExceptionToBeThrown() throws SQLException {
        Mockito.when(jobRoleDao.getJobRoleById(1)).thenThrow(SQLException.class);
        assertThrows(SQLException.class, () -> jobRoleService.getJobRoleById(1));
    }

    @Test
    public void getJobRoleById_whenDaoReturnsNull_ExpectDoesNotExistExceptionToBeThrown() throws SQLException {
        Mockito.when(jobRoleDao.getJobRoleById(1)).thenReturn(null);
        assertThrows(DoesNotExistException.class, () -> jobRoleService.getJobRoleById(1));
    }

    @Test
    public void testGetFilteredJobRolesEmptyList() throws SQLException, ResultSetException {
        when(jobRoleDao.getFilteredJobRoles(any())).thenReturn(Collections.emptyList());
        assertThrows(
                DoesNotExistException.class, () -> jobRoleService.getFilteredJobRoles(new JobRoleFilteredRequest()));
    }

    @Test
    public void getFilteredJobRoles_WhenDaoThrowsSQLException_ExpectSQLExceptionToBeThrown()
            throws ResultSetException, SQLException {
        when(jobRoleDao.getFilteredJobRoles(any())).thenThrow(SQLException.class);
        assertThrows(SQLException.class, () -> jobRoleService.getFilteredJobRoles(any()));
    }

    @Test
    public void getFilteredJobRoles_shouldReturnListOfJobRolesResponse()
            throws SQLException, ResultSetException, DoesNotExistException {
        JobRoleFilteredRequest jobRoleFilteredRequest = new JobRoleFilteredRequest();
        jobRoles.add(
                new JobRole(3, "test", "Belfast", "testCapability", "testBand", Date.valueOf("2000-10-10"), "open"));
        when(jobRoleDao.getFilteredJobRoles(jobRoleFilteredRequest)).thenReturn(jobRoles);

        List<JobRoleResponse> expected = new ArrayList<>();
        expected.add(new JobRoleResponse(
                3, "test", "Belfast", "testCapability", "testBand", Date.valueOf("2000-10-10"), "open"));
        var result = jobRoleService.getFilteredJobRoles(jobRoleFilteredRequest);
        assertEqualLists(expected, result);
    }

    @Test
    public void getAllUserApplications_shouldReturnJobListForGivenUser() throws SQLException, DoesNotExistException {
        String email = "admin";

        List<JobRoleApplication> expectedJobRoleApplications = new ArrayList<>();
        expectedJobRoleApplications.add(new JobRoleApplication(1, "Engineer", "hired"));
        expectedJobRoleApplications.add(new JobRoleApplication(2, "Trainee", "rejected"));

        Mockito.when(jobRoleDao.getUserJobRoleApplications(email)).thenReturn(expectedJobRoleApplications);

        List<JobRoleApplication> resultJobRoleApplications = jobRoleService.getAllUserApplications(email);
        assertEqualLists(expectedJobRoleApplications, resultJobRoleApplications);
    }

    @Test
    public void getAllUserApplication_shouldThrowExpection_whenListIsEmpty()
            throws DoesNotExistException, SQLException {
        String email = "email";
        when(jobRoleDao.getUserJobRoleApplications(email)).thenReturn(Collections.emptyList());
        assertThrows(DoesNotExistException.class, () -> jobRoleService.getAllUserApplications(email));
    }

    @Test
    public void getJobRolesFromCsv_MapperShouldConvertFileToCSVModel()
            throws Exception, FileTooBigException, InvalidFileTypeException, FileNeededException {

        JobRoleDetails jobRoleDetails = new JobRoleDetails(
                "RoleName",
                "Location",
                "Capability",
                "Band",
                Date.valueOf("2024-09-30"),
                "StatusName",
                "Description",
                "Responsibilities",
                "SharepointUrl",
                5);

        when(jobRoleDao.getCapabilityIdByName("Capability")).thenReturn(1);
        when(jobRoleDao.getBandIdByName("Band")).thenReturn(1);
        when(jobRoleDao.getStatusIdByName("StatusName")).thenReturn(1);

        JobRoleDetailsCSV resultJobRoleDetailsCSV = JobRoleMapper.toJobRolesCSV(jobRoleDetails, jobRoleDao);

        JobRoleDetailsCSV expectedJobRoleDetailsCSV = new JobRoleDetailsCSV(
                "RoleName",
                "Location",
                1,
                1,
                Date.valueOf("2024-09-30"),
                "Description",
                "Responsibilities",
                "SharepointUrl",
                1,
                5);

        assertEquals(expectedJobRoleDetailsCSV, resultJobRoleDetailsCSV);
    }
}
