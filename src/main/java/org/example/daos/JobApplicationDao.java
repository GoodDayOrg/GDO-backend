package org.example.daos;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.example.models.ApplicationStatusId;

public class JobApplicationDao {
    private static final String BUCKET_NAME = "good-day-org-recruitment";

    public void applyForRole(
            final int jobRoleId, final String userEmail, final byte[] fileBytes, final ObjectMetadata metadata)
            throws SQLException, IOException {

        String key = createKey(jobRoleId, userEmail);
        System.out.println("key: " + key);
        uploadFileToS3(key, fileBytes, metadata);
        addJobApplication(jobRoleId, userEmail);
    }

    private String createKey(final int jobRoleId, final String userEmail) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return "applications/" + jobRoleId + "/" + userEmail + "-resume" + timestamp + ".pdf";
    }

    public void uploadFileToS3(final String key, final byte[] fileBytes, final ObjectMetadata metadata) {

        AmazonS3 amazonS3Client = AmazonS3Connector.getAmazonS3Client();
        amazonS3Client.putObject(BUCKET_NAME, key, new ByteArrayInputStream(fileBytes), metadata);
    }

    public boolean existsByIdAndUserEmail(final int jobRoleId, final String userEmail) throws SQLException {
        try (Connection connection = DatabaseConnector.getConnection()) {

            String query = "SELECT * FROM job_application WHERE Email = ? AND jobRoleId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userEmail);
            statement.setInt(2, jobRoleId);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        }
    }

    public void addJobApplication(final int jobRoleId, final String userEmail) throws SQLException {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "INSERT INTO job_application (Email, jobRoleId, statusApplicationId) VALUES (?,?,"
                    + ApplicationStatusId.IN_PROGRESS.getStatusId() + ")";

            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, userEmail);
            statement.setInt(2, jobRoleId);
            System.out.println(statement);
            statement.executeUpdate();
        }
    }
}
