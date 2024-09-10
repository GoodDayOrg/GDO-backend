package org.example.daos;

import org.example.exceptions.ResultSetException;
import org.example.models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.example.exceptions.ResultSetException;
import org.example.models.JobRole;
import org.example.models.JobRoleDetails;

public class JobRoleDao {
    public List<JobRole> getAllJobRoles()
            throws SQLException, ResultSetException {
        List<JobRole> jobRoles = new ArrayList<>();

        try (Connection connection = DatabaseConnector.getConnection()) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(baseQuery() + ";");

            while (resultSet.next()) {
                addJobRoleFromResultSet(jobRoles, resultSet);
            }
        }

        return jobRoles;
    }

    public List<JobRole> getFilteredJobRoles(
            final JobRoleFilteredRequest jobRequest)
            throws SQLException, ResultSetException {
        List<JobRole> jobRoles = new ArrayList<>();
        StringBuilder query = new StringBuilder(baseQuery());
        List<Object> parameters = new ArrayList<>();

        applyFiltersToQuery(jobRequest, query, parameters);
        executeFilteredJobQuery(query, parameters, jobRoles);

        return jobRoles;
    }

    private static String baseQuery() {
        return "SELECT jobRoleId, roleName, location, statusId, statusName, capabilityName, "
                + "bandName, closingDate\n"
                + "FROM job_roles\n"
                + "INNER JOIN capability USING(capabilityId)\n"
                + "INNER JOIN band USING(bandId)\n"
                + "INNER JOIN status using(statusId)\n"
                + "WHERE statusName = 'open'";
    }

    private void applyFiltersToQuery(
            final JobRoleFilteredRequest jobRequest, final StringBuilder query,
            final List<Object> parameters) {
        if (jobRequest.getRoleName() != null &&
                !jobRequest.getRoleName().isBlank()) {
            query.append(" AND roleName LIKE ?");
            parameters.add(jobRequest.getLikeRoleName());
        }
        applyFilter(jobRequest.getJobRoleLocation(), "location", query,
                parameters);
        applyFilter(jobRequest.getCapabilityName(), "capabilityName", query,
                parameters);
        applyFilter(jobRequest.getBandName(), "bandName", query, parameters);
        if (jobRequest.getClosingDate() != null) {
            query.append(" AND closingDate < ?");
            parameters.add(jobRequest.getClosingDate());
        }

        query.append(";");
    }

    private void executeFilteredJobQuery(
            final StringBuilder query, final List<Object> parameters,
            final List<JobRole> jobRoles)
            throws SQLException, ResultSetException {
        try (Connection connection = DatabaseConnector.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    query.toString())) {
                for (int i = 0; i < parameters.size(); i++) {
                    if (parameters.get(i) instanceof String) {
                        statement.setString(i + 1, (String) parameters.get(i));
                    } else if (parameters.get(i) instanceof Integer) {
                        statement.setInt(i + 1, (Integer) parameters.get(i));
                    } else if (parameters.get(i) instanceof java.sql.Date) {
                        statement.setDate(i + 1,
                                (java.sql.Date) parameters.get(i));
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
                System.out.println(statement);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    addJobRoleFromResultSet(jobRoles, resultSet);
                }
            }
        }
    }

    private <T> void applyFilter(
            final List<T> values, final String key, final StringBuilder query,
            final List<Object> parameters) {
        if (isPresent(values)) {
            query.append(" AND ").append(key).append(" IN (");
            query.append(
                    String.join(", ", Collections.nCopies(values.size(), "?")));
            query.append(")");
            parameters.addAll(values);
        }
    }

    private <E> boolean isPresent(final List<E> list) {
        return list != null && !list.isEmpty();
    }

    private void addJobRoleFromResultSet(final List<JobRole> jobRoles,
                                         final ResultSet resultSet)
            throws ResultSetException {
        JobRole jobRole;
        try {
            jobRole = new JobRole(
                    resultSet.getInt("jobRoleId"),
                    resultSet.getString("roleName"),
                    resultSet.getString("location"),
                    resultSet.getString("capabilityName"),
                    resultSet.getString("bandName"),
                    resultSet.getDate("closingDate"),
                    resultSet.getString("statusName"));
        } catch (SQLException e) {
            throw new ResultSetException(e.getMessage());
        }

        jobRoles.add(jobRole);
    }

    public JobRoleDetails getJobRoleById(final int id) throws SQLException {
        try (Connection connection = DatabaseConnector.getConnection()) {

            String query =
                    "SELECT roleName, location, capabilityName, bandName, closingDate, statusName, "
                            +
                            "description, responsibilities, sharepointUrl, numberOfOpenPositions\n"
                            + "FROM job_roles\n"
                            + "INNER JOIN capability USING(capabilityId)\n"
                            + "INNER JOIN band USING(bandId)\n"
                            + "INNER JOIN status using(statusId)\n"
                            + "WHERE jobRoleId = ?;";

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return new JobRoleDetails(
                        resultSet.getString("roleName"),
                        resultSet.getString("location"),
                        resultSet.getString("capabilityName"),
                        resultSet.getString("bandName"),
                        resultSet.getDate("closingDate"),
                        resultSet.getString("statusName"),
                        resultSet.getString("description"),
                        resultSet.getString("responsibilities"),
                        resultSet.getString("sharepointUrl"),
                        resultSet.getInt("numberOfOpenPositions"));
            }
        }
        return null;
    }

    public void importMultipleJobRoles(List<JobRoleDetailsCSV> detailedJobRoles) throws SQLException {
        String query = "INSERT INTO job_roles (roleName, location, capabilityId, bandId, closingDate, description, "
                + "responsibilities, sharepointUrl, statusId, numberOfOpenPositions) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        System.out.println("hgdjhssdhjhjbdsdshjb");
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (JobRoleDetailsCSV jobRole : detailedJobRoles) {
                System.out.println(jobRole.getStatusId());
                preparedStatement.setString(1, jobRole.getRoleName());
                preparedStatement.setString(2, jobRole.getJobRoleLocation());
                preparedStatement.setInt(3, jobRole.getCapabilityId());
                preparedStatement.setInt(4, jobRole.getBandId());
                preparedStatement.setDate(5, jobRole.getClosingDate());
                preparedStatement.setString(6, jobRole.getDescription());
                preparedStatement.setString(7, jobRole.getResponsibilities());
                preparedStatement.setString(8, jobRole.getSharepointUrl());
                preparedStatement.setInt(9, jobRole.getStatusId());
                preparedStatement.setInt(10, jobRole.getNumberOfOpenPositions());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        }
    }


    public int getCapabilityIdByName(String name) throws SQLException {

        try (Connection connection = DatabaseConnector.getConnection()) {

            String query = "SELECT capabilityId FROM capability WHERE capabilityName = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Check if there is a result
                if (resultSet.next()) {
                    // Retrieve the capabilityId from the result set
                    return resultSet.getInt("capabilityId");
                } else {
                    // Handle the case where no rows were returned
                    throw new SQLException("No capability found with name: " + name);
                }
            }
        }
    }


    public int getBandIdByName(String name) throws SQLException {


        try (Connection connection = DatabaseConnector.getConnection()) {

            String query = "SELECT bandId FROM band WHERE bandName = ?;";
             PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Check if there is a result
                if (resultSet.next()) {
                    // Retrieve the bandId from the result set
                    return resultSet.getInt("bandId");
                } else {
                    // Handle the case where no rows were returned
                    throw new SQLException("No band found with name: " + name);
                }
            }
        }
    }


    public int getStatusIdByName(String name) throws SQLException {
        try (Connection connection = DatabaseConnector.getConnection()) {

            String query = "SELECT statusId FROM status WHERE statusName = ?;";
             PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Check if there is a result
                if (resultSet.next()) {
                    // Retrieve the statusApplicationId from the result set
                    return resultSet.getInt("statusId");
                } else {
                    // Handle the case where no rows were returned
                    throw new SQLException("No status found with name: " + name);
                }
            }
        }
    }

    public List<JobRoleApplication> getUserJobRoleApplications(final String email)
            throws SQLException {
        List<JobRoleApplication> jobRoleApplications = new ArrayList<>();

        try (Connection connection = DatabaseConnector.getConnection()) {
            Statement statement = connection.createStatement();

            String query = "SELECT jr.jobRoleId, jr.roleName, aps.statusApplicationName\n"
                    + "FROM job_application ja\n"
                    + "INNER JOIN application_status aps ON ja.statusApplicationId = aps.statusApplicationId\n"
                    + "INNER JOIN job_roles jr ON ja.jobRoleId = jr.jobRoleId\n"
                    + "INNER JOIN User u ON ja.Email = u.Email\n"
                    + "WHERE u.Email = '" + email + "';";
            System.out.println(query);
            ResultSet resultSet = statement.executeQuery(
                    query
            );

            while (resultSet.next()) {
                JobRoleApplication jobRoleApplication = new JobRoleApplication(
                        resultSet.getInt("jobRoleId"),
                        resultSet.getString("roleName"),
                        resultSet.getString("statusApplicationName")
                );

                jobRoleApplications.add(jobRoleApplication);
            }
        }
        return jobRoleApplications;

    }
}
