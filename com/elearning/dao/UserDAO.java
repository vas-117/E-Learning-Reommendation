package com.elearning.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64; // ✅ Import for simple hashing

import com.elearning.database.DBConnection;
import com.elearning.model.User;

public class UserDAO {

    private Connection connection;

    public UserDAO() {
        this.connection = DBConnection.getConnection();
    }

    // ✅ Updated to save Password
    public int addUser(User user) {
        // First, check if email already exists
        if (getUserByEmail(user.getEmail()) != null) {
            return -1; // User exists
        }

        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        int newUserId = -1;

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            
            // ✅ BONUS: Encrypt the password before saving
            String encodedPassword = Base64.getEncoder().encodeToString(user.getPassword().getBytes());
            statement.setString(3, encodedPassword);
            
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newUserId = generatedKeys.getInt(1); 
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newUserId;
    }

    public User getUserByEmail(String email) {
        User user = null;
        String sql = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setId(resultSet.getInt("user_id"));
                    user.setName(resultSet.getString("name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password")); // Store the encrypted password
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // ✅ NEW METHOD: Verify Login Credentials
    public User loginUser(String email, String rawPassword) {
        User user = getUserByEmail(email);
        if (user != null) {
            // Encrypt the input password and compare it with the stored encrypted password
            String encodedInput = Base64.getEncoder().encodeToString(rawPassword.getBytes());
            
            if (encodedInput.equals(user.getPassword())) {
                return user; // Success!
            }
        }
        return null; // Failed
    }
}
