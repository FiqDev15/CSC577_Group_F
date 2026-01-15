package com.edusummarize.models;

import com.edusummarize.utils.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin Model Class - Extends User (Inheritance)
 * 
 * CLASS DIAGRAM: Admin → User (Inheritance/Generalization)
 * 
 * CLASS DIAGRAM ATTRIBUTES:
 * - adminLevel: int (additional to User attributes)
 * 
 * CLASS DIAGRAM OPERATIONS:
 * - monitorSystemPerformance(): Map<String, Object>
 * - manageUserAccess(): boolean
 * - viewSubscription(): List<Subscription>
 */
public class Admin extends User {
    // Additional attribute from class diagram
    private int adminLevel;  // Level of administrative access (1=Basic, 2=Moderate, 3=Full)
    
    // Constructors
    public Admin() {
        super();
        this.setRole("Admin");  // Override role to Admin
        this.adminLevel = 1;    // Default admin level
    }
    
    public Admin(int userID, String name, String email, String password, int adminLevel) {
        super(userID, name, email, password);
        this.setRole("Admin");
        this.adminLevel = adminLevel;
    }
    
    // CLASS DIAGRAM OPERATIONS
    
    /**
     * Operation: monitorSystemPerformance()
     * Checks system performance metrics
     * @return Map containing system statistics
     */
    public Map<String, Object> monitorSystemPerformance() {
        Map<String, Object> metrics = new HashMap<>();
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            // Get total users count
            String userCountSql = "SELECT COUNT(*) as total FROM users";
            PreparedStatement stmt = conn.prepareStatement(userCountSql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                metrics.put("totalUsers", rs.getInt("total"));
            }
            
            // Get premium users count
            String premiumCountSql = "SELECT COUNT(*) as total FROM users WHERE is_premium = 1";
            stmt = conn.prepareStatement(premiumCountSql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                metrics.put("premiumUsers", rs.getInt("total"));
            }
            
            // Calculate free users
            int totalUsers = (int) metrics.getOrDefault("totalUsers", 0);
            int premiumUsers = (int) metrics.getOrDefault("premiumUsers", 0);
            metrics.put("freeUsers", totalUsers - premiumUsers);
            
            // Calculate revenue (Premium users * RM50)
            metrics.put("monthlyRevenue", premiumUsers * 50.0);
            
            metrics.put("status", "success");
            
        } catch (SQLException e) {
            metrics.put("status", "error");
            metrics.put("errorMessage", e.getMessage());
            System.err.println("Error monitoring system performance: " + e.getMessage());
        }
        
        return metrics;
    }
    
    /**
     * Operation: manageUserAccess()
     * Manages user account access (activate/deactivate)
     * @param userID User ID to manage
     * @param activate true to activate, false to deactivate
     * @return true if successful
     */
    public boolean manageUserAccess(int userID, boolean activate) {
        if (this.adminLevel < 2) {
            System.err.println("Insufficient admin level. Level 2+ required.");
            return false;
        }
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            // Update user account status
            String sql = "UPDATE users SET account_status = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, activate);
            stmt.setInt(2, userID);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("User " + userID + " access " + (activate ? "activated" : "deactivated"));
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error managing user access: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Operation: viewSubscription()
     * Views all subscription details in the system
     * @return List of subscription information
     */
    public List<Map<String, Object>> viewSubscription() {
        List<Map<String, Object>> subscriptions = new ArrayList<>();
        
        if (this.adminLevel < 1) {
            System.err.println("Insufficient admin level.");
            return subscriptions;
        }
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT id, full_name, email, is_premium, pdf_count, " +
                        "subscription_start, subscription_end, created_at " +
                        "FROM users ORDER BY created_at DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> subscription = new HashMap<>();
                subscription.put("userID", rs.getInt("id"));
                subscription.put("userName", rs.getString("full_name"));
                subscription.put("email", rs.getString("email"));
                subscription.put("planType", rs.getBoolean("is_premium") ? "Premium" : "Free");
                subscription.put("pdfCount", rs.getInt("pdf_count"));
                subscription.put("startDate", rs.getTimestamp("subscription_start"));
                subscription.put("endDate", rs.getTimestamp("subscription_end"));
                subscription.put("joinedDate", rs.getTimestamp("created_at"));
                
                subscriptions.add(subscription);
            }
            
        } catch (SQLException e) {
            System.err.println("Error viewing subscriptions: " + e.getMessage());
        }
        
        return subscriptions;
    }
    
    public boolean manageUserSubscription(int userID, boolean activateSubscription) {
        if (this.adminLevel < 2) {
            System.err.println("Insufficient admin level. Level 2+ required to manage subscriptions.");
            return false;
        }
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql;
            PreparedStatement stmt;
            
            if (activateSubscription) {
                // Activate Premium subscription
                sql = "UPDATE users SET is_premium = 1, " +
                      "subscription_start = NOW(), " +
                      "subscription_end = DATE_ADD(NOW(), INTERVAL 30 DAY) " +
                      "WHERE id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userID);
            } else {
                // Deactivate subscription (set to Free)
                sql = "UPDATE users SET is_premium = 0, " +
                      "subscription_start = NULL, " +
                      "subscription_end = NULL " +
                      "WHERE id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userID);
            }
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("User " + userID + " subscription " + 
                                 (activateSubscription ? "activated (Premium)" : "deactivated (Free)"));
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error managing user subscription: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Additional helper method: Check if user is admin
     */
    public boolean isAdmin() {
        return this.getRole().equals("Admin") && adminLevel > 0;
    }
    
    /**
     * Additional helper method: Get all users
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT * FROM users ORDER BY created_at DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("id"));
                user.setName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setSubscriptionStatus(rs.getBoolean("is_premium") ? "Premium" : "Free");
                user.setAccountStatus(rs.getBoolean("account_status"));
                user.setPdfCount(rs.getInt("pdf_count"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                
                users.add(user);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
        }
        
        return users;
    }
    
    // Getters and Setters
    
    public int getAdminLevel() {
        return adminLevel;
    }
    
    public void setAdminLevel(int adminLevel) {
        this.adminLevel = adminLevel;
    }
    
    @Override
    public String toString() {
        return "Admin{" +
                "userID=" + getUserID() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", role='" + getRole() + '\'' +
                ", adminLevel=" + adminLevel +
                '}';
    }
}
