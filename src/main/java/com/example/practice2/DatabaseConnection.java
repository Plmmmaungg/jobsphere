package com.example.practice2;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/job_sphere_db";
            String user = "root"; // 🔹 change this to your MySQL username
            String password = "06242006"; // 🔹 change this to your MySQL password


            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Database connected successfully!");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Database connection failed.");
            return null;
        }
    }
}



