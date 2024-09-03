package org.example.services;

import org.example.daos.JobRoleDao;
import org.example.exceptions.DoesNotExistException;
import org.example.exceptions.ResultSetException;
import org.example.models.JobRole;
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

import static org.example.utils.Utils.compareLists;
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
        when(jobRoleDao.getAllJobRoles()).thenReturn(jobRoles);

        List<JobRoleResponse> expected = new ArrayList<>();
        expected.add(
                new JobRoleResponse("test", "Belfast", "testCapability", "testBand", Date.valueOf("2000-10-10"), "open")
        );
        var result = jobRoleService.getAllJobRoles();
        compareLists(expected, result);

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
    public void testGetFilteredJobRolesEmptyList() throws SQLException, ResultSetException {
        // Given
        when(jobRoleDao.getFilteredJobRoles(any())).thenReturn(Collections.emptyList());
        // When & Then
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
                new JobRoleResponse("test", "Belfast", "testCapability", "testBand", Date.valueOf("2000-10-10"), "open")
        );
        var result = jobRoleService.getFilteredJobRoles(jobRoleFilteredRequest);
        compareLists(expected, result);

    }
}