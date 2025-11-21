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
            System.out.println("1. Register (New User)");
            System.out.println("2. Login (Existing User)");
            System.out.println("3. Browse Courses by Category"); 
            System.out.println("4. View My Enrolled Courses");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1: signUpAndRecommend(scanner); break;
                    case 2: loginAndRecommend(scanner); break;
                    case 3: browseByCategory(scanner); break;
                    case 4: viewEnrolledCoursesOption(scanner); break;
                    case 5: 
                        System.out.println("Thank you using the system. Goodbye!");
                        scanner.close();
                        return;
                    default: System.out.println("Invalid option. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); 
            }
        }
    }
   
    private static void signUpAndRecommend(Scanner scanner) {
        System.out.println("\n--- User Sign Up ---");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Create a password: "); 
        String password = scanner.nextLine();

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(password);
        
        int newUserId = userDAO.addUser(newUser);

        if (newUserId == -1) {
            System.out.println("\n[INFO] An account with this email already exists.");
            System.out.println("Please try logging in instead.");
            return;
        }

        System.out.println("Welcome, " + name + "! Your registration is complete.");
        newUser.setId(newUserId);
        startUserSession(scanner, newUser);
    }
    private static void loginAndRecommend(Scanner scanner) {
        System.out.println("\n--- User Login ---");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: "); 
        String password = scanner.nextLine();
        
        User user = userDAO.loginUser(email, password);
        
        if (user != null) {
            System.out.println("Welcome back, " + user.getName() + "!");
            startUserSession(scanner, user);
        } else {
            System.out.println("❌ Login Failed. Incorrect email or password.");
        }
    }

    private static void startUserSession(Scanner scanner, User user) {
        showUserHistory(user);
        processCourseSelection(scanner, user.getId());
    }

    private static void showUserHistory(User user) {
        List<Course> enrolledCourses = courseDAO.getEnrolledCourses(user.getId());
        
        if (!enrolledCourses.isEmpty()) {
            System.out.println("\n=========================================");
            System.out.println("       MY ENROLLED COURSES (" + enrolledCourses.size() + ")");
            System.out.println("=========================================");
            for (Course c : enrolledCourses) {
                System.out.println("✓ " + c.getTitle() + " [" + c.getCategory() + "]");
            }
            System.out.println("=========================================");
        } else {
            System.out.println("\n(You are not enrolled in any courses yet.)");
        }
    }

    private static void viewEnrolledCoursesOption(Scanner scanner) {
        System.out.println("\n--- View My History ---");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        
        User user = userDAO.loginUser(email, password);
        
        if (user != null) {
            System.out.println("Fetching history for: " + user.getName());
            showUserHistory(user);
        } else {
            System.out.println("❌ Access Denied. Incorrect credentials.");
        }
    }

    private static void processCourseSelection(Scanner scanner, int userId) {
        while (true) {
            System.out.println("\n=== Course Selection Menu ===");
            System.out.println("1. Search for a Course ");
            System.out.println("2. View All Courses ");
            System.out.println("3. Logout / Main Menu ");
            System.out.print("Select an option: ");
            
            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
                continue;
            }

            if (choice == 3) return;

            List<Course> coursesToDisplay;

            if (choice == 1) {
                System.out.print("Enter keyword: ");
                String keyword = scanner.nextLine();
                coursesToDisplay = courseDAO.searchCourses(keyword);
                if (coursesToDisplay.isEmpty()) {
                    System.out.println(" No courses found.");
                    continue;
                }
            } else if (choice == 2) {
                coursesToDisplay = courseDAO.getAllCourses();
            } else {
                System.out.println("Invalid option.");
                continue;
            }

            System.out.println("\n--- Found " + coursesToDisplay.size() + " Courses ---");
            for (Course course : coursesToDisplay) {
                System.out.println("ID: " + course.getId() + " - " + course.getTitle() + " [" + course.getCategory() + "]");
            }

            System.out.print("\nEnter Course ID to enroll (or 0 to cancel): ");
            int selectedCourseId = -1;
            try {
                selectedCourseId = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }

            if (selectedCourseId > 0) {
                if (interactionDAO.isEnrolled(userId, selectedCourseId)) {
                    System.out.println("\n⚠️ You are ALREADY enrolled in this course!");
                } else {
                    interactionDAO.addInteraction(userId, selectedCourseId);
                    System.out.println("\n✅ Successfully enrolled!");
                    showRecommendations(userId);
                    return; 
                }
            }
        }
    }

    private static void showRecommendations(int userId) {
        System.out.println("\nBased on your selection, we recommend:");
        List<Course> recommendations = recommender.getRecommendationsForUser(userId);

        if (recommendations.isEmpty()) {
            System.out.println("No specific recommendations right now.");
        } else {
            for (Course course : recommendations) {
                System.out.println("★ RECOMMENDED: " + course.getTitle() + " [" + course.getCategory() + "]");
            }
        }
    }

    private static void browseByCategory(Scanner scanner) {
        System.out.println("\n--- Browse by Category ---");
        System.out.println("Categories: Java, Python, Database, Web Development, Cloud, AI/ML");
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        List<Course> courses = courseDAO.getCoursesByCategory(category);
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            for (Course course : courses) {
                System.out.println("ID: " + course.getId() + " - " + course.getTitle());
            }
        }
    }
}
