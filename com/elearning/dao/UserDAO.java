package com.elearning.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.elearning.database.DBConnection;
import com.elearning.model.User;

public class UserDAO {

    private Connection connection;

    public UserDAO() {
        this.connection = DBConnection.getConnection();
    }
    public int addUser(User user) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        int newUserId = -1;

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newUserId = generatedKeys.getInt(1); }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newUserId;
    }
}