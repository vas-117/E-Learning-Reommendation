package com.elearning.main;

import java.sql.Connection;
import java.sql.SQLException; 
import com.elearning.database.DBConnection;

public class TestDB {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("Successfully connected to the database!");
        } else {
            System.out.println("Failed to connect to the database.");
            return;
        }
        try {
            DBConnection.closeConnection();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            System.err.println("An error occurred while closing the connection.");
            e.printStackTrace();
        }
    }
}