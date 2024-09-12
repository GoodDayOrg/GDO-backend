package org.example.services;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.example.daos.JobApplicationDao;
import org.example.daos.JobRoleDao;
import org.example.exceptions.AlreadyExistsException;
import org.example.exceptions.DoesNotExistException;
import org.example.exceptions.Entity;
import org.example.exceptions.FileNeededException;
import org.example.exceptions.FileTooBigException;

import org.example.exceptions.InvalidFileTypeException;

import org.example.exceptions.FileUploadException;

import org.example.exceptions.ResultSetException;
import org.example.mappers.JobRoleMapper;
import org.example.models.JobRole;
import org.example.models.JobRoleApplication;
import org.example.models.JobRoleDetails;
import org.example.models.JobRoleDetailsCSV;
import org.example.models.JobRoleFilteredRequest;
import org.example.models.JobRoleResponse;
import org.example.validators.JobApplicationValidator;
import org.example.validators.JobRoleImportValidator;

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


    public void getJobRolesFromCsv(final InputStream inputStream, final String fileName)
            throws IOException, FileNeededException, FileTooBigException, InvalidFileTypeException {
        List<JobRoleDetailsCSV> jobRoleDetailsList = new ArrayList<>();

        byte[] fileBytes = JobRoleImportValidator.readInputStream(inputStream);

        JobRoleImportValidator.validateCsvFile(fileBytes, fileName);

        try (InputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);
             BufferedReader reader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                     .build()) {

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                if (line.length == 0 || (line.length == 1 && line[0].trim().isEmpty())) {
                    continue;
                }
                String roleName = line[0];
                String location = line[1];
                String capability = line[2];
                String band = line[3];
                Date closingDate = Date.valueOf(line[4]);
                String description = line[5];
                String responsibilities = line[6];
                String sharepointUrl = line[7];
                String statusName = line[8];
                int openPositions = Integer.parseInt(line[9]);

                JobRoleDetails jobRoleDetails = new JobRoleDetails(
                        roleName,
                        location,
                        capability,
                        band,
                        closingDate,
                        statusName,
                        description,
                        responsibilities,
                        sharepointUrl,
                        openPositions);

                jobRoleDetailsList.add(JobRoleMapper.toJobRolesCSV(jobRoleDetails, jobRoleDao));
            }
            jobRoleDao.importMultipleJobRoles(jobRoleDetailsList);

        } catch (SQLException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
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
