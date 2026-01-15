package com.edusummarize.models;

import java.sql.Timestamp;
import java.util.regex.Pattern;

/**
 * User Model Class - Matches Class Diagram
 * 
 * CLASS DIAGRAM ATTRIBUTES:
 * - userID: int
 * - name: String
 * - email: String
 * - password: String
 * - role: String
 * - accountStatus: boolean
 * - subscriptionStatus: String
 * - pdfCount: int
 * 
 * CLASS DIAGRAM OPERATIONS:
 * - login(): boolean
 * - logout(): boolean
 * - register(): boolean
 * - validateEmail(): boolean
 * - validatePassword(): boolean
 * - checkSubscriptionLimit(): boolean
 */
public class User {
    // Attributes (from class diagram)
    private int userID;
    private String name;
    private String email;
    private String password;
    private String role;                    // New: User role (e.g., 'User', 'Admin')
    private boolean accountStatus;          // Active/Inactive
    private String subscriptionStatus;      // Free/Premium
    
    // Additional attributes for subscription tracking
    private int pdfCount;
    private Timestamp createdAt;
    
    // Constructors
    public User() {
        this.role = "User";  // Default role
        this.accountStatus = true;
        this.subscriptionStatus = "Free";
        this.pdfCount = 0;
    }
    
    public User(int userID, String name, String email, String password) {
        this();
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    
    // CLASS DIAGRAM OPERATIONS
    
    /**
     * Operation: login()
     * Validates user credentials for login
     */
    public boolean login(String inputPassword) {
        // In real implementation, this would check hashed password
        return this.password.equals(inputPassword) && this.accountStatus;
    }
    
    /**
     * Operation: logout()
     * Handles user logout process
     */
    public boolean logout() {
        // Clear sensitive data or perform logout actions
        return true;
    }
    
    /**
     * Operation: register()
     * Validates user data for registration
     */
    public boolean register() {
        return validateEmail() && validatePassword() && name != null && !name.trim().isEmpty();
    }
    
    /**
     * Operation: validateEmail()
     * Checks if email format is valid
     */
    public boolean validateEmail() {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    
    /**
     * Operation: validatePassword()
     * Checks password strength (minimum 6 characters, contains letter and number)
     */
    public boolean validatePassword() {
        if (password == null || password.length() < 6) {
            return false;
        }
        // Check if password contains at least one letter and one number
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        return hasLetter && hasDigit;
    }
    
    /**
     * Operation: checkSubscriptionLimit()
     * Checks if user has reached PDF upload limit (Free users: 100)
     */
    public boolean checkSubscriptionLimit() {
        if (subscriptionStatus.equals("Premium")) {
            return true;  // No limit for premium users
        }
        return pdfCount < 100;  // Free users limited to 100 PDFs
    }
    
    // Getters and Setters
    
    public int getUserID() {
        return userID;
    }
    
    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public boolean isAccountStatus() {
        return accountStatus;
    }
    
    public void setAccountStatus(boolean accountStatus) {
        this.accountStatus = accountStatus;
    }
    
    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }
    
    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }
    
    public int getPdfCount() {
        return pdfCount;
    }
    
    public void setPdfCount(int pdfCount) {
        this.pdfCount = pdfCount;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", subscriptionStatus='" + subscriptionStatus + '\'' +
                ", pdfCount=" + pdfCount +
                '}';
    }
}
