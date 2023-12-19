package app.form.calculator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private String DB_URL;
    private String DB_USER;
    private String DB_PASSWORD;

    public Database(String DB_URL, String DB_USER, String DB_PASSWORD){
        this.DB_URL = DB_URL;
        this.DB_USER = DB_USER;
        this.DB_PASSWORD = DB_PASSWORD;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public boolean loginUser(String username, String password) {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM users WHERE name = ? AND pass = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean registerUser(String username, String password) {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO Users (name, pass) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);

                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            return false;
        }
    }
}

