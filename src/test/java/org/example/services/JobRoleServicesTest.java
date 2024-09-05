package org.example.services;

import org.example.daos.JobRoleDao;
import org.example.exceptions.DoesNotExistException;
import org.example.exceptions.ResultSetException;
import org.example.models.JobRole;
import org.example.models.JobRoleDetails;
import org.example.models.JobRoleResponse;
import org.example.models.JobRoleFilteredRequest;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.example.utils.Utils.assertEqualLists;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class JobRoleServicesTest {
    List<JobRole> jobRoles;
    JobRoleDao jobRoleDao = Mockito.mock(JobRoleDao.class);
    JobRoleService jobRoleService = new JobRoleService(jobRoleDao);

    @BeforeEach
    public void jobRolesListClean() {
        jobRoles = new ArrayList<>();
    }

    @Test
    public void getAllJobRoles_shouldReturnListOfJobRolesResponse() throws SQLException, ResultSetException, DoesNotExistException {
        jobRoles.add(
                new JobRole(3, "test", "Belfast", "testCapability", "testBand", Date.valueOf("2000-10-10"), "open")
        );
        jobRoles.add(
                new JobRole(2, "test2", "Belfast", "testCapability2", "testBand2", Date.valueOf("2000-10-11"), "closed")
        );
        when(jobRoleDao.getAllJobRoles()).thenReturn(jobRoles);

        List<JobRoleResponse> expected = new ArrayList<>();
        expected.add(
                new JobRoleResponse(3, "test", "Belfast", "testCapability", "testBand", Date.valueOf("2000-10-10"), "open")
        );

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
    public void getJobRoleById_shouldReturnJobRoleDetails()
            throws SQLException, DoesNotExistException {
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
    public void getJobRoleById_whenDaoThrowsSQLException_ExpectSQLExceptionToBeThrown()
            throws SQLException {
        Mockito.when(jobRoleDao.getJobRoleById(1)).thenThrow(SQLException.class);
        assertThrows(SQLException.class, () -> jobRoleService.getJobRoleById(1));
    }

    @Test
    public void getJobRoleById_whenDaoReturnsNull_ExpectDoesNotExistExceptionToBeThrown()
            throws SQLException {
        Mockito.when(jobRoleDao.getJobRoleById(1)).thenReturn(null);
        assertThrows(DoesNotExistException.class, () -> jobRoleService.getJobRoleById(1));
    }

    @Test
    public void testGetFilteredJobRolesEmptyList() throws SQLException, ResultSetException {
        when(jobRoleDao.getFilteredJobRoles(any())).thenReturn(Collections.emptyList());
        assertThrows(DoesNotExistException.class, () ->  jobRoleService.getFilteredJobRoles(
                new JobRoleFilteredRequest()));
  }
  @Test
    public void getFilteredJobRoles_WhenDaoThrowsSQLException_ExpectSQLExceptionToBeThrown() throws ResultSetException, SQLException {
      when(jobRoleDao.getFilteredJobRoles(any())).thenThrow(SQLException.class);
      assertThrows(SQLException.class, () -> jobRoleService.getFilteredJobRoles(any()));
  }

    @Test
    public void getFilteredJobRoles_shouldReturnListOfJobRolesResponse() throws SQLException, ResultSetException, DoesNotExistException {
        JobRoleFilteredRequest jobRoleFilteredRequest = new JobRoleFilteredRequest();
        jobRoles.add(
                new JobRole(3, "test", "Belfast", "testCapability", "testBand", Date.valueOf("2000-10-10"), "open")
        );
        when(jobRoleDao.getFilteredJobRoles(jobRoleFilteredRequest)).thenReturn(jobRoles);

        List<JobRoleResponse> expected = new ArrayList<>();
        expected.add(
                new JobRoleResponse(3, "test", "Belfast", "testCapability", "testBand", Date.valueOf("2000-10-10"), "open")
        );
        var result = jobRoleService.getFilteredJobRoles(jobRoleFilteredRequest);
        assertEqualLists(expected, result);
    }
}
