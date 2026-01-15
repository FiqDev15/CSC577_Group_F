package com.edusummarize.servlets;

import com.edusummarize.utils.DatabaseUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validate input
        if (fullName == null || fullName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "All fields are required.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        // Check password match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        // Check password validation (length, letter, and number)
        if (password.length() < 6) {
            request.setAttribute("errorMessage", "Password must be at least 6 characters.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        
        if (!hasLetter || !hasDigit) {
            request.setAttribute("errorMessage", "Password must contain at least one letter and one number.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            // Check if email already exists
            String checkSql = "SELECT id FROM users WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                request.setAttribute("errorMessage", "Email already registered. Please login.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            
            // Insert new user with free plan (is_premium=0, pdf_count=0, role='User', admin_level=0)
            String insertSql = "INSERT INTO users (full_name, email, password, role, admin_level, is_premium, pdf_count) VALUES (?, ?, ?, 'User', 0, 0, 0)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, fullName);
            insertStmt.setString(2, email);
            insertStmt.setString(3, password); // In production, use hashed passwords!
            
            int rowsInserted = insertStmt.executeUpdate();
            
            if (rowsInserted > 0) {
                // Registration successful
                request.setAttribute("successMessage", "Registration successful! Please login.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Registration failed. Please try again.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("register.jsp");
    }
}
