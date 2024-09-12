package org.example.services;

import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import org.example.daos.JobApplicationDao;
import org.example.daos.JobRoleDao;
import org.example.exceptions.AlreadyExistsException;
import org.example.exceptions.DoesNotExistException;
import org.example.exceptions.Entity;
import org.example.exceptions.FileNeededException;
import org.example.exceptions.FileTooBigException;
import org.example.exceptions.FileUploadException;
import org.example.exceptions.ResultSetException;
import org.example.mappers.JobRoleMapper;
import org.example.models.JobRole;
import org.example.models.JobRoleApplication;
import org.example.models.JobRoleDetails;
import org.example.models.JobRoleFilteredRequest;
import org.example.models.JobRoleResponse;
import org.example.validators.JobApplicationValidator;

public class JobRoleService {

    private final JobRoleDao jobRoleDao;
    private final JobApplicationDao jobApplicationDao;
    private final JobApplicationValidator jobApplicationValidator;

    public JobRoleService(
            final JobRoleDao jobRoleDao,
            final JobApplicationDao jobApplicationDao,
            final JobApplicationValidator jobApplicationValidator) {
        this.jobRoleDao = jobRoleDao;
        this.jobApplicationDao = jobApplicationDao;
        this.jobApplicationValidator = jobApplicationValidator;
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

    public List<JobRoleApplication> getAllUserApplications(final String email)
            throws SQLException, DoesNotExistException {
        List<JobRoleApplication> jobRoleApplications = jobRoleDao.getUserJobRoleApplications(email);
        if (jobRoleApplications.isEmpty()) {
            throw new DoesNotExistException(Entity.JOB_APPLICATION);
        }
        return jobRoleApplications;
    }

    public List<JobRoleResponse> getFilteredJobRoles(final JobRoleFilteredRequest jobRoleFilteredRequest)
            throws SQLException, DoesNotExistException, ResultSetException {
        List<JobRoleResponse> jobRoleResponses =
                JobRoleMapper.toResponse(jobRoleDao.getFilteredJobRoles(jobRoleFilteredRequest));
        if (jobRoleResponses.isEmpty()) {
            throw new DoesNotExistException(Entity.USER);
        }
        return jobRoleResponses;
    }

    public void applyForRole(final int jobRoleId, final String userEmail, final InputStream fileInputStream)
            throws DoesNotExistException, SQLException, FileTooBigException, AlreadyExistsException,
                    FileNeededException, IOException, FileUploadException {

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.addUserMetadata("jobRoleId", String.valueOf(jobRoleId));
        metadata.addUserMetadata("userEmail", userEmail);
        metadata.setContentType("application/pdf");

        byte[] fileBytes = jobApplicationValidator.validateAndProduceByteArray(jobRoleId, userEmail, fileInputStream);
        metadata.setContentLength(fileBytes.length);
        jobApplicationDao.applyForRole(jobRoleId, userEmail, fileBytes, metadata);
    }
}
