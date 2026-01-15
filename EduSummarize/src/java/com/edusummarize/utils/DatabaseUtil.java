package com.edusummarize.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseUtil - Database Connection and Schema Management
 * 
 * Database Schema Class Diagram:
 * =========================================
 * 
 * 1. USERS TABLE (User entity)
 *    Columns:
 *    - id (userID): int PRIMARY KEY
 *    - full_name (name): String
 *    - email: String UNIQUE
 *    - password: String
 *    - is_premium (accountStatus): boolean (0=Free, 1=Premium)
 *    - pdf_count: int (tracks usage for free users)
 *    - subscription_start: timestamp
 *    - subscription_end: timestamp
 *    - created_at: timestamp
 *    - updated_at: timestamp
 * 
 *    Relationships:
 *    - 1 User → 0..1 Subscription (via is_premium, subscription fields)
 *    - 1 User → 1..* Documents (via user_id in summaries table)
 *    - Admin extends User (adminLevel > 0 indicates admin)
 * 
 * 2. SUMMARIES TABLE (Document + SummaryResult entities)
 *    Columns:
 *    - id (summaryID/documentID): int PRIMARY KEY
 *    - user_id: int FOREIGN KEY → users(id)
 *    - file_name (fileName): String
 *    - file_type (fileType): String
 *    - original_length (fileSize): int
 *    - summary_text (summaryText): TEXT
 *    - summary_length: String
 *    - created_at (uploadDate/generatedDate): timestamp
 * 
 *   Relationships:
 *    - Many summaries → 1 User (via user_id)
 *    - 1 Document → 1 SummaryResult (same record)
 * 
 * 3. SUBSCRIPTION (Virtual entity - represented in users table)
 *    Attributes in users table:
 *    - is_premium: boolean (1=active subscription)
 *    - subscription_start: timestamp
 *    - subscription_end: timestamp
 *    - pdf_count: int (usage tracking)
 * 
 *    Business Rules:
 *    - Free: 100 PDF limit, RM0/month
 *    - Premium: Unlimited PDFs, RM50/month
 * 
 * 4. PAYMENT (Not yet implemented as separate table)
 *    Would track:
 *    - paymentID, subscriptionID, userID
 *    - amount, paymentDate, transactionStatus
 * 
 * REFERENTIAL INTEGRITY:
 * - summaries.user_id → users.id (CASCADE DELETE)
 * - When user deleted, all their summaries deleted
 */
public class DatabaseUtil {
    
    // Database configuration for XAMPP
    private static final String DB_URL = "jdbc:mysql://localhost:3306/edusummarize_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // XAMPP default - no password
    
    // Load MySQL JDBC Driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }
    
    /**
     * Get a database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    /**
     * Close database connection
     * @param conn Connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Test database connection
     * @return true if connection successful
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Increment PDF count for a user
     * @param userId User ID
     * @throws SQLException if update fails
     */
    public static void incrementPdfCount(int userId) throws SQLException {
        String sql = "UPDATE users SET pdf_count = pdf_count + 1 WHERE id = ?";
        try (Connection conn = getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
}
