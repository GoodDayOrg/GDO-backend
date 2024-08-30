package org.example.daos;

import org.example.models.LoginRequest;
import org.example.models.User;

import java.sql.*;


public class AuthDao {

    public User getUser(final LoginRequest loginRequest)
            throws SQLException {
        try (Connection connection = DatabaseConnector.getConnection()) {

            String query = "SELECT Email, Password, role_id FROM `User` "
                    + "WHERE Email = ?;";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, loginRequest.getEmail());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return new User(
                        resultSet.getString("Email"),
                        resultSet.getString("Password"),
                        resultSet.getInt("role_id"));
            }
        }
        return null;
    }

}
