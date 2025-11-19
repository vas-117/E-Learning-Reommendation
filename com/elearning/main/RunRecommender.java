package com.elearning.main;

import java.util.List;
import com.elearning.model.Course;
import com.elearning.recommendation.RecommendationService;

public class RunRecommender {
    public static void main(String[] args) {
        RecommendationService recommender = new RecommendationService();
        
        int userIdToRecommendFor = 1;
        
        System.out.println("Generating recommendations for User ID: " + userIdToRecommendFor);
        
        List<Course> recommendedCourses = recommender.getRecommendationsForUser(userIdToRecommendFor);
        
        if (recommendedCourses.isEmpty()) {
            System.out.println("No new recommendations found for this user.");
        } else {
            System.out.println("Found " + recommendedCourses.size() + " recommendations:");
            for (Course course : recommendedCourses) {
                System.out.println("  -> " + course.toString());
            }
        }
    }
}