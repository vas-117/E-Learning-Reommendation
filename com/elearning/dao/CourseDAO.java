package com.elearning.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.elearning.database.DBConnection;
import com.elearning.model.Course;

public class CourseDAO {

    private Connection connection;
    public CourseDAO() {
        this.connection = DBConnection.getConnection();
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("course_id"));
                course.setTitle(resultSet.getString("title"));
                course.setCategory(resultSet.getString("category"));
                course.setDescription(resultSet.getString("description"));
                
                courses.add(course);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
    
    // ✅ THIS IS THE NEW METHOD YOU WERE MISSING
    public List<Course> getCoursesByCategory(String category) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE category = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Course course = new Course();
                    course.setId(resultSet.getInt("course_id"));
                    course.setTitle(resultSet.getString("title"));
                    course.setCategory(resultSet.getString("category"));
                    course.setDescription(resultSet.getString("description"));
                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
}