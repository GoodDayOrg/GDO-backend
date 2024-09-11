package org.example.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.exceptions.DoesNotExistException;
import org.example.exceptions.Entity;
import org.example.models.JobRoleApplicationResponse;

public class JobApplicationDao {
    public List<JobRoleApplicationResponse> getJobApplicationsById(final int roleId) throws SQLException {
        List<JobRoleApplicationResponse> jobRoleApplicationResponses = new ArrayList<>();

        try (Connection connection = DatabaseConnector.getConnection()) {

            String query = "SELECT Email, roleName, statusApplicationName FROM job_application \n"
                    + "INNER JOIN job_roles USING(jobRoleId)\n"
                    + "INNER JOIN application_status USING(statusApplicationId)\n"
                    + "WHERE jobRoleId = ?";

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, roleId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                addJobRoleApplicationResponseToList(jobRoleApplicationResponses, resultSet);
            }
        }

        return jobRoleApplicationResponses;
    }

    private void addJobRoleApplicationResponseToList(
            final List<JobRoleApplicationResponse> jobRoleApplicationResponses, final ResultSet resultSet)
            throws SQLException {
        jobRoleApplicationResponses.add(new JobRoleApplicationResponse(
                resultSet.getString("Email"),
                resultSet.getString("roleName"),
                resultSet.getString("statusApplicationName")));
    }

    public boolean existsByIdAndEmail(final int roleId, final String userEmail) throws SQLException {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "SELECT * FROM job_application WHERE jobRoleId = ? AND Email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roleId);
            statement.setString(2, userEmail);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        }
    }

    public void changeStatus(final int roleId, final String userEmail, final int statusId)
            throws SQLException, DoesNotExistException {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "UPDATE job_application SET statusApplicationId = ? WHERE jobRoleId = ? AND Email = ?";
            final int index1 = 1;
            final int index2 = 2;
            final int index3 = 3;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(index1, statusId);
            statement.setInt(index2, roleId);
            statement.setString(index3, userEmail);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new DoesNotExistException(Entity.APPLICATION);
            }
        }
    }

    public int getStatusId(final String statusName) throws SQLException {
        try (Connection connection = DatabaseConnector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT statusApplicationId FROM application_status WHERE statusApplicationName = ?");
            statement.setString(1, statusName);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("statusApplicationId");
            }
            return 0;
        }
    }
}
