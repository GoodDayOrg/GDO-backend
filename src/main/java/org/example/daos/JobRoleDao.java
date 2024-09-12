package org.example.daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.example.exceptions.ResultSetException;
import org.example.models.JobRole;
import org.example.models.JobRoleApplication;
import org.example.models.JobRoleDetails;
import org.example.models.JobRoleDetailsCSV;
import org.example.models.JobRoleFilteredRequest;

public class JobRoleDao {

    public List<JobRole> getAllJobRoles() throws SQLException, ResultSetException {
        List<JobRole> jobRoles = new ArrayList<>();

        try (Connection connection = DatabaseConnector.getConnection()) {
            assert connection != null;
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(baseQuery() + ";");

            while (resultSet.next()) {
                addJobRoleFromResultSet(jobRoles, resultSet);
            }
        }

        return jobRoles;
    }

    public List<JobRole> getFilteredJobRoles(final JobRoleFilteredRequest jobRequest)
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

    private void executeFilteredJobQuery(
            final StringBuilder query, final List<Object> parameters, final List<JobRole> jobRoles)
            throws SQLException, ResultSetException {
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query.toString())) {

            setParameters(statement, parameters);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                addJobRoleFromResultSet(jobRoles, resultSet);
            }
        }
    }

    private void setParameters(final PreparedStatement statement, final List<Object> parameters) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            Object param = parameters.get(i);
            if (param instanceof String) {
                statement.setString(i + 1, (String) param);
            } else if (param instanceof Integer) {
                statement.setInt(i + 1, (Integer) param);
            } else if (param instanceof Date) {
                statement.setDate(i + 1, (Date) param);
            } else {
                throw new IllegalArgumentException(
                        "Unsupported parameter type: " + param.getClass().getName());
            }
        }
    }

    private boolean isNullOrEmpty(final Object value) {
        return value == null || value.toString().isBlank();
    }

    private boolean isClosingDate(final String key, final Object value) {
        return "closingDate".equals(key) && value instanceof Date;
    }

    private void handleClosingDate(
            final StringBuilder query, final List<Object> parameters, final String key, final Object value) {
        query.append(" AND ").append(key).append(" < ?");
        parameters.add(value);
    }

    private boolean isCommaSeparatedString(final Object value) {
        return value instanceof String && ((String) value).contains(",");
    }

    private void handleSingleValue(
            final StringBuilder query, final List<Object> parameters, final String key, final Object value) {
        query.append(" AND ").append(key).append(" LIKE ?");
        parameters.add(value);
    }

    private void handleCommaSeparatedValues(
            final StringBuilder query, final List<Object> parameters, final String key, final String value) {
        String[] values = value.split(",");
        query.append(" AND ").append(key).append(" IN (");
        query.append(String.join(", ", Collections.nCopies(values.length, "?")));
        query.append(")");

        for (String val : values) {
            parameters.add(val.trim());
        }
    }

    private void appendFilter(
            final StringBuilder query, final List<Object> parameters, final String key, final Object value) {
        if (isNullOrEmpty(value)) {
            return;
        }

        if (isClosingDate(key, value)) {
            handleClosingDate(query, parameters, key, value);
        } else if (isCommaSeparatedString(value)) {
            handleCommaSeparatedValues(query, parameters, key, (String) value);
        } else {
            handleSingleValue(query, parameters, key, value);
        }
    }

    private void applyFiltersToQuery(
            final JobRoleFilteredRequest jobRequest, final StringBuilder query, final List<Object> parameters) {
        appendFilter(query, parameters, "roleName", jobRequest.getLikeRoleName());
        appendFilter(query, parameters, "location", jobRequest.getJobRoleLocation());
        appendFilter(query, parameters, "capabilityName", jobRequest.getCapabilityName());
        appendFilter(query, parameters, "bandName", jobRequest.getBandName());
        appendFilter(query, parameters, "closingDate", jobRequest.getClosingDate());

        query.append(";");
    }

    private <E> boolean isPresent(final List<E> list) {
        return list != null && !list.isEmpty();
    }

    private void addJobRoleFromResultSet(final List<JobRole> jobRoles, final ResultSet resultSet)
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

            String query = "SELECT roleName, location, capabilityName, bandName, closingDate, statusName, "
                    + "description, responsibilities, sharepointUrl, numberOfOpenPositions\n"
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

    public void importMultipleJobRoles(final List<JobRoleDetailsCSV> detailedJobRoles) throws SQLException {

        String query = "INSERT INTO job_roles (roleName, location, capabilityId, bandId, closingDate, description, "
                + "responsibilities, sharepointUrl, statusId, numberOfOpenPositions) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (JobRoleDetailsCSV jobRole : detailedJobRoles) {
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

    public int getCapabilityIdByName(final String name) throws SQLException {

        try (Connection connection = DatabaseConnector.getConnection()) {

            String query = "SELECT capabilityId FROM capability WHERE capabilityName = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("capabilityId");
                } else {
                    throw new SQLException("No capability found with name: " + name);
                }
            }
        }
    }

    public int getBandIdByName(final String name) throws SQLException {

        try (Connection connection = DatabaseConnector.getConnection()) {

            String query = "SELECT bandId FROM band WHERE bandName = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("bandId");
                } else {
                    throw new SQLException("No band found with name: " + name);
                }
            }
        }
    }

    public int getStatusIdByName(final String name) throws SQLException {
        try (Connection connection = DatabaseConnector.getConnection()) {

            String query = "SELECT statusId FROM status WHERE statusName = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("statusId");
                } else {
                    throw new SQLException("No status found with name: " + name);
                }
            }
        }
    }

    public boolean existsOpenById(final int jobRoleId) throws SQLException {
        try (Connection connection = DatabaseConnector.getConnection()) {

            String query = "SELECT * FROM job_roles WHERE jobRoleId = ? AND statusId = 1";
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, jobRoleId);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        }
    }

    public List<JobRoleApplication> getUserJobRoleApplications(final String email) throws SQLException {
        List<JobRoleApplication> jobRoleApplications = new ArrayList<>();

        try (Connection connection = DatabaseConnector.getConnection()) {
            Statement statement = connection.createStatement();

            String query = "SELECT jr.jobRoleId, jr.roleName, aps.statusApplicationName\n"
                    + "FROM job_application ja\n"
                    + "INNER JOIN application_status aps ON ja.statusApplicationId = aps.statusApplicationId\n"
                    + "INNER JOIN job_roles jr ON ja.jobRoleId = jr.jobRoleId\n"
                    + "INNER JOIN User u ON ja.Email = u.Email\n"
                    + "WHERE u.Email = '" + email + "';";

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                JobRoleApplication jobRoleApplication = new JobRoleApplication(
                        resultSet.getInt("jobRoleId"),
                        resultSet.getString("roleName"),
                        resultSet.getString("statusApplicationName"));

                jobRoleApplications.add(jobRoleApplication);
            }
        }
        return jobRoleApplications;
    }
}