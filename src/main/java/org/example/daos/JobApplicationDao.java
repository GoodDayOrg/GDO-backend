package org.example.daos;

import org.example.exceptions.DoesNotExistException;
import org.example.exceptions.Entity;
import org.example.models.JobRole;
import org.example.models.JobRoleApplicationResponse;

import javax.xml.crypto.Data;
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

            statement.setInt(1, roleId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                addJobRoleApplicationResponseToList(jobRoleApplicationResponses, resultSet);
            }
        }

        return jobRoleApplicationResponses;
    }

    private void addJobRoleApplicationResponseToList(List<JobRoleApplicationResponse> jobRoleApplicationResponses,
                                                     ResultSet resultSet)
            throws SQLException {
        jobRoleApplicationResponses.add(new JobRoleApplicationResponse(
                resultSet.getString("Email"),
                resultSet.getString("roleName"),
                resultSet.getString("statusApplicationName")
        ));
    }

    public boolean existsByIdAndEmail(final int roleId,
                                      final String userEmail) throws SQLException {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "SELECT * FROM job_applications WHERE jobRoleId = ? AND Email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roleId);
            statement.setString(2, userEmail);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        }
    }

    public void changeStatus(final int roleId,
                             final String userEmail,
                             final String status) throws SQLException, DoesNotExistException {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "UPDATE job_applications SET status = ? WHERE jobRoleId = ? AND Email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,status);
            statement.setInt(2,roleId);
            statement.setString(3,userEmail);
            int rowsUpdated = statement.executeUpdate();

            if(rowsUpdated == 0){
                throw new DoesNotExistException(Entity.APPLICATION);
            }
        }
    }

    public List<String> getStatusNames() throws SQLException {
        List<String> statusNames = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("SELECT statusApplicationName FROM application_status;");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                statusNames.add(resultSet.getString("statusApplicationName"));
            }
        }

        return statusNames;
    }
}
