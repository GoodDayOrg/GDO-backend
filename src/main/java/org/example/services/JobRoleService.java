package org.example.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.example.daos.JobRoleDao;
import org.example.exceptions.*;
import org.example.mappers.JobRoleMapper;
import org.example.models.*;
import com.opencsv.CSVReader;
import org.example.validators.JobRoleImportValidator;

public class JobRoleService {

    JobRoleDao jobRoleDao;
    JobRoleMapper jobRoleMapper;

    public JobRoleService(final JobRoleDao jobRoleDao, final JobRoleMapper jobRoleMapper) {
        this.jobRoleDao = jobRoleDao;
        this.jobRoleMapper = jobRoleMapper;
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
            throw new DoesNotExistException(Entity.APPLICATION);
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

    public void getJobRolesFromCsv(InputStream inputStream) throws IOException, FileNeededException, FileTooBigException {
        List<JobRoleDetailsCSV> jobRoleDetailsList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                     .build()) {

             JobRoleImportValidator.validateCsvFile(inputStream);

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                String roleName = line[0];
                String location = line[1];
                String capability = line[2];
                String band = line[3];
                Date closingDate = Date.valueOf("2000-10-20");
                System.out.println(closingDate);
                String description = line[5];
                String responsibilities = line[6];
                String sharepointUrl = line[7];
                String statusName = line[8];
                int openPositions = Integer.parseInt(line[9]);
                JobRoleDetails jobRoleDetails = new JobRoleDetails(
                        roleName, location, capability, band, closingDate, statusName, description, responsibilities,
                        sharepointUrl, openPositions

                );
                jobRoleDetailsList.add(jobRoleMapper.toJobRolesCSV(jobRoleDetails, jobRoleDao));
                }


            jobRoleDao.importMultipleJobRoles(jobRoleDetailsList);
        } catch (CsvValidationException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
