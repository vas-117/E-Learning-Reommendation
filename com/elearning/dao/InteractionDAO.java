package com.elearning.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.elearning.database.DBConnection;
import com.elearning.model.Interaction;

public class InteractionDAO {

    private Connection connection;

    public InteractionDAO() {
        this.connection = DBConnection.getConnection();
    }
    
    public List<Interaction> getInteractionsByUserId(int userId) {
        List<Interaction> interactions = new ArrayList<>();
        String sql = "SELECT * FROM interactions WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Interaction interaction = new Interaction();
                    interaction.setInteractionId(resultSet.getInt("interaction_id"));
                    interaction.setUserId(resultSet.getInt("user_id"));
                    interaction.setCourseId(resultSet.getInt("course_id"));
                    interaction.setInteractionType(resultSet.getString("interaction_type"));
                    interactions.add(interaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interactions;
    }

    public void addInteraction(int userId, int courseId) {
        String sql = "INSERT INTO interactions (user_id, course_id, interaction_type) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, courseId);
            statement.setString(3, "selected");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ NEW METHOD: Check if a user is already enrolled in a course
    public boolean isEnrolled(int userId, int courseId) {
        String sql = "SELECT 1 FROM interactions WHERE user_id = ? AND course_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Returns true if a record is found
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
