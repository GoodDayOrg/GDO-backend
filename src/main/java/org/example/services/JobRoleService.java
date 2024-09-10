package org.example.services;

import java.sql.SQLException;
import java.util.List;
import org.example.daos.JobRoleDao;
import org.example.exceptions.DoesNotExistException;
import org.example.exceptions.Entity;
import org.example.exceptions.ResultSetException;
import org.example.mappers.JobRoleMapper;
import org.example.models.JobRole;
import org.example.models.JobRoleDetails;
import org.example.models.JobRoleFilteredRequest;
import org.example.models.JobRoleResponse;

public class JobRoleService {

    JobRoleDao jobRoleDao;

    public JobRoleService(final JobRoleDao jobRoleDao) {
        this.jobRoleDao = jobRoleDao;
    }

    public List<JobRole> testConnection() throws SQLException, ResultSetException {
        return jobRoleDao.getAllJobRoles();
    }

    public List<JobRoleResponse> getAllJobRoles() throws SQLException, DoesNotExistException, ResultSetException {
        List<JobRoleResponse> jobRoleResponses = JobRoleMapper.toResponse(jobRoleDao.getAllJobRoles());
        if (jobRoleResponses.isEmpty()) {
            throw new DoesNotExistException(Entity.JOB_ROLE);
        }
        return jobRoleResponses;
    }

    public JobRoleDetails getJobRoleById(final int id) throws SQLException, DoesNotExistException {
        JobRoleDetails jobRoleDetails = jobRoleDao.getJobRoleById(id);
        if (jobRoleDetails == null) {
            throw new DoesNotExistException(Entity.JOB_ROLE);
        }
        return jobRoleDetails;
    }

    public List<JobRoleResponse> getFilteredJobRoles(final JobRoleFilteredRequest jobRoleFilteredRequest)
            throws SQLException, DoesNotExistException, ResultSetException {
        List<JobRoleResponse> jobRoleResponses =
                JobRoleMapper.toResponse(jobRoleDao.getFilteredJobRoles(jobRoleFilteredRequest));
        if (jobRoleResponses.isEmpty()) {
            throw new DoesNotExistException(Entity.JOB_ROLE);
        }
        return jobRoleResponses;
    }


}
