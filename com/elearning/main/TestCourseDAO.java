package com.elearning.main;

import java.util.List;
import com.elearning.dao.CourseDAO;
import com.elearning.model.Course;

public class TestCourseDAO {

    public static void main(String[] args) {
        CourseDAO courseDAO = new CourseDAO();

        System.out.println("Fetching all courses from the database...");
        List<Course> courses = courseDAO.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found in the database.");
        } else {
            System.out.println("Found " + courses.size() + " courses:");
            for (Course course : courses) {
                System.out.println(course.toString());
            }
        }
    }
}