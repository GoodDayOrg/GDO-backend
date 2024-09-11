package org.example.daos;

import org.example.models.JobRole;
import org.example.models.JobRoleApplicationResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JobApplicationDao {
    public List<JobRoleApplicationResponse> getJobApplicationsById(int roleId) throws SQLException {
        List<JobRoleApplicationResponse> jobRoleApplicationResponses = new ArrayList<>();

        try (Connection connection = DatabaseConnector.getConnection()) {

            String query = "SELECT Email, roleName, statusApplicationName FROM job_application" +
                    " INNER JOIN job_roles USING(jobRoleId)" +
                    "INNER JOIN application_status USING(statusApplicationId)" +
                    "WHERE jobRoleId = ?";

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1,roleId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                addJobRoleApplicationResponseToList(jobRoleApplicationResponses, resultSet);
            }
        }

        return jobRoleApplicationResponses;
    }

    private void addJobRoleApplicationResponseToList(List<JobRoleApplicationResponse> jobRoleApplicationResponses, ResultSet resultSet)
            throws SQLException {
        jobRoleApplicationResponses.add(new JobRoleApplicationResponse(
                resultSet.getString("Email"),
                resultSet.getString("roleName"),
                resultSet.getString("statusApplicationName")
        ));
    }
}
