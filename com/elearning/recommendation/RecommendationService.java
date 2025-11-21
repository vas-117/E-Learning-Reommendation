package com.elearning.recommendation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.elearning.dao.CourseDAO;
import com.elearning.dao.InteractionDAO;
import com.elearning.model.Course;
import com.elearning.model.Interaction;

public class RecommendationService {

    private CourseDAO courseDAO;
    private InteractionDAO interactionDAO;

    public RecommendationService() {
        this.courseDAO = new CourseDAO();
        this.interactionDAO = new InteractionDAO();
    }

    public List<Course> getRecommendationsForUser(int userId) {

        List<Interaction> userInteractions = interactionDAO.getInteractionsByUserId(userId);

        Set<Integer> interactedCourseIds = userInteractions.stream()
            .map(Interaction::getCourseId)
            .collect(Collectors.toSet());

        Set<String> interestedCategories = new HashSet<>();
        List<Course> allCourses = courseDAO.getAllCourses();
        for (Course course : allCourses) {
            if (interactedCourseIds.contains(course.getId())) {
                interestedCategories.add(course.getCategory());
            }
        }
        
        // System.out.println("User is interested in categories: " + interestedCategories); // Removed debug print
        List<Course> recommendations = new ArrayList<>();
        for (Course course : allCourses) {
            if (!interactedCourseIds.contains(course.getId()) && interestedCategories.contains(course.getCategory())) {
                recommendations.add(course);
            }
        }

        return recommendations;
    }
}
