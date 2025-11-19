package com.elearning.main;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import com.elearning.dao.CourseDAO;
import com.elearning.dao.InteractionDAO;
import com.elearning.dao.UserDAO;
import com.elearning.model.Course;
import com.elearning.model.User;
import com.elearning.recommendation.RecommendationService;

public class ELearningConsole { 

    private static UserDAO userDAO = new UserDAO();
    private static CourseDAO courseDAO = new CourseDAO();
    private static InteractionDAO interactionDAO = new InteractionDAO();
    private static RecommendationService recommender = new RecommendationService();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n===== E-Learning Recommendation System =====");
            System.out.println("1. Sign Up & Get Recommendations");
            System.out.println("2. Browse Courses by Category"); 
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        signUpAndRecommend(scanner);
                        break;
                    case 2:
                        browseByCategory(scanner); 
                        break;
                    case 3:
                        System.out.println("Thank you for using the system. Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); 
            }
        }
    }
   
    private static void browseByCategory(Scanner scanner) {
        System.out.println("\n--- Browse by Category ---");
        System.out.println("Available Categories: Java, Python, Database, Web Development, Cloud, AI/ML, Cybersecurity, DevOps, Design");
        System.out.print("Please enter a category name to browse: ");
        String category = scanner.nextLine();
        
        List<Course> courses = courseDAO.getCoursesByCategory(category);
        
        if (courses.isEmpty()) {
            System.out.println("No courses found for the category: " + category);
        } else {
            System.out.println("\n--- Courses in '" + category + "' ---");
            for (Course course : courses) {
                System.out.println("ID: " + course.getId() + " - " + course.getTitle());
                System.out.println("   Description: " + course.getDescription());
            }
        }
    }

    private static void signUpAndRecommend(Scanner scanner) {
        System.out.println("\n--- User Sign Up ---");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        int newUserId = userDAO.addUser(newUser);

        if (newUserId == -1) {
            System.out.println("User registration failed. Please try again.");
            return;
        }

        System.out.println("Welcome, " + name + "! Your registration is complete.");

        System.out.println("\n--- Available Courses ---");
        List<Course> allCourses = courseDAO.getAllCourses();
        for (Course course : allCourses) {
            System.out.println("ID: " + course.getId() + " - " + course.getTitle() + " [" + course.getCategory() + "]");
            System.out.println("   Description: " + course.getDescription());
        }

        int selectedCourseId = -1;
        while (selectedCourseId == -1) {
            System.out.print("\nPlease select a course by entering its ID: ");
            try {
                selectedCourseId = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number corresponding to a course ID.");
                scanner.nextLine(); 
            }
        }
        scanner.nextLine(); 

        interactionDAO.addInteraction(newUserId, selectedCourseId);
        System.out.println("Great choice!");

        System.out.println("\nBased on your selection, here are your recommended courses:");
        List<Course> recommendations = recommender.getRecommendationsForUser(newUserId);

        if (recommendations.isEmpty()) {
            System.out.println("No other courses found in the same category.");
        } else {
            for (Course course : recommendations) {
                System.out.println("  -> " + course.getTitle() + " [" + course.getCategory() + "]");
                System.out.println("     Description: " + course.getDescription());
            }
        }
    }
}