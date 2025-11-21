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

    public List<Course> getEnrolledCourses(int userId) {
        List<Course> courses = new ArrayList<>();
        // Uses DISTINCT to avoid duplicates in the history view
        String sql = "SELECT DISTINCT c.* FROM courses c JOIN interactions i ON c.course_id = i.course_id WHERE i.user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            
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

    // ✅ NEW METHOD: Search for courses by keyword
    public List<Course> searchCourses(String keyword) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE title LIKE ? OR category LIKE ? OR description LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String query = "%" + keyword + "%";
            statement.setString(1, query);
            statement.setString(2, query);
            statement.setString(3, query);
            
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
