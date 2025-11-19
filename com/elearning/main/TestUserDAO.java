package com.elearning.main;

import com.elearning.dao.UserDAO;
import com.elearning.model.User;

public class TestUserDAO {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        // Add a user
        User user = new User(0, "Vasundhara", "vasu@example.com", "AI, Java, Web");
        userDAO.addUser(user);

        // Display all users
        for (User u : userDAO.getAllUsers()) {
            System.out.println(u);
        }
    }
}
