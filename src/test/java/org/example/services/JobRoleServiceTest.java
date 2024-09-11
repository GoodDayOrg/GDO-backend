package org.example.services;

import static org.example.utils.Utils.assertEqualLists;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.example.daos.JobRoleDao;
import org.example.exceptions.*;
import org.example.mappers.JobRoleMapper;
import org.example.models.*;
import org.example.validators.JobRoleImportValidator;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JobRoleServiceTest {
    List<JobRole> jobRoles;
    JobRoleMapper jobRoleMapper;
    JobRoleDao jobRoleDao = Mockito.mock(JobRoleDao.class);
    JobRoleService jobRoleService = new JobRoleService(jobRoleDao, jobRoleMapper);

    @BeforeEach
    public void jobRolesListClean() {
        jobRoles = new ArrayList<>();
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
    public void getAllUserApplications_shouldReturnJobListForGivenUser()
            throws SQLException, DoesNotExistException {
        String email = "admin";

        List<JobRoleApplication> expectedJobRoleApplications = new ArrayList<>();
        expectedJobRoleApplications.add(
                new JobRoleApplication(1, "Engineer", "hired")
        );
        expectedJobRoleApplications.add(
                new JobRoleApplication(2, "Trainee", "rejected")
        );

        Mockito.when(jobRoleDao.getUserJobRoleApplications(email)).thenReturn(expectedJobRoleApplications);

        List<JobRoleApplication> resultJobRoleApplications = jobRoleService.getAllUserApplications(email);
        assertEqualLists(expectedJobRoleApplications, resultJobRoleApplications);
     }

    @Test
    public void getAllUserApplication_shouldThrowExpection_whenListIsEmpty() throws DoesNotExistException, SQLException {
        String email = "email";
        when(jobRoleDao.getUserJobRoleApplications(email)).thenReturn(Collections.emptyList());
        assertThrows(
                DoesNotExistException.class, () -> jobRoleService.getAllUserApplications(email));
    }

    @Test
    public void getJobRolesFromCsv_MapperShouldConvertFileToCSVModel() throws Exception, FileTooBigException, InvalidFileTypeException, FileNeededException {

        JobRoleDetails jobRoleDetails = new JobRoleDetails(
                "RoleName", "Location", "Capability", "Band", Date.valueOf("2024-09-30"),
                "StatusName", "Description", "Responsibilities", "SharepointUrl", 5
        );

        when(jobRoleDao.getCapabilityIdByName("Capability")).thenReturn(1);
        when(jobRoleDao.getBandIdByName("Band")).thenReturn(1);
        when(jobRoleDao.getStatusIdByName("StatusName")).thenReturn(1);

        JobRoleDetailsCSV resultJobRoleDetailsCSV = JobRoleMapper.toJobRolesCSV(jobRoleDetails, jobRoleDao);

        JobRoleDetailsCSV expectedJobRoleDetailsCSV = new JobRoleDetailsCSV("RoleName", "Location", 1,
                1, Date.valueOf("2024-09-30"), "Description", "Responsibilities",
                "SharepointUrl", 1, 5);

        assertEquals(expectedJobRoleDetailsCSV, resultJobRoleDetailsCSV);
    }
}
