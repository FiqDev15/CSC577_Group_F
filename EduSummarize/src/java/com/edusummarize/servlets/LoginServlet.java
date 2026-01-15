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
import javax.servlet.http.HttpSession;

/**
 * LoginServlet - Handles User Authentication
 * 
 * CLASS DIAGRAM RELATIONSHIPS:
 * ============================
 * This servlet manages the User entity which has the following relationships:
 * 
 * 1. User → Subscription (Directed Association 1 to 0..1)
 *    - Each User can have 0 or 1 Subscription
 *    - Represented by: is_premium, pdf_count, subscription_start, subscription_end fields
 *    - Navigation: User can access their Subscription status
 * 
 * 2. User → Document (Directed Association 1 to 1..*)
 *    - Each User can create multiple Documents for summarization
 *    - Represented by: user_id foreign key in summaries table
 *    - Navigation: User can access all their Documents
 * 
 * 3. Admin → User (Inheritance/Generalization)
 *    - Admin is a specialized type of User
 *    - Admin has additional privileges: adminLevel, monitorSystemPerformance(), manageUserAccount(), viewSubscription()
 *    - Represented by: adminLevel field (admin users have adminLevel > 0)
 * 
 * User Attributes (from database):
 * - userID: int (Primary Key)
 * - name: String
 * - email: String
 * - password: String
 * - accountStatus: boolean (represented as is_premium)
 * - subscriptionStatus: String (Free/Premium based on is_premium)
 * 
 * User Operations:
 * - login(): Authenticates user and creates session
 * - logout(): Terminates user session
 * - register(): Creates new user account
 */
public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Validate input
        if (email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please enter both email and password.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            // Query to check user credentials (includes role, admin_level, and account_status)
            String sql = "SELECT id, full_name, email, role, admin_level, is_premium, pdf_count, account_status FROM users WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password); // In production, use hashed passwords!
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Check if account is deactivated
                boolean accountStatus = rs.getBoolean("account_status");
                if (!accountStatus) {
                    request.setAttribute("errorMessage", "Your account has been deactivated. Please contact administrator.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }
                // Login successful - create session
                HttpSession session = request.getSession();
                // CLASS DIAGRAM: Admin → User (Inheritance/Generalization)
                // Admin extends User (adminLevel > 0 indicates admin privileges)
                session.setAttribute("userId", rs.getInt("id"));
                session.setAttribute("userName", rs.getString("full_name"));
                session.setAttribute("userEmail", rs.getString("email"));
                
                // Load role and admin level (with null safety)
                String role = rs.getString("role");
                if (role == null || role.trim().isEmpty()) {
                    role = "User";  // Default to User if null
                }
                int adminLevel = rs.getInt("admin_level");
                session.setAttribute("userRole", role);
                session.setAttribute("adminLevel", adminLevel);
                
                // Check if user is admin
                boolean isAdmin = "Admin".equals(role) && adminLevel > 0;
                session.setAttribute("isAdmin", isAdmin);
                
                // CLASS DIAGRAM: User → Subscription (Directed Association, 1 to 0..1)
                session.setAttribute("isPremium", rs.getBoolean("is_premium"));
                session.setAttribute("pdfCount", rs.getInt("pdf_count"));
                
                // Redirect based on user role
                if (isAdmin) {
                    response.sendRedirect("admin_dashboard.jsp");
                } else {
                    response.sendRedirect("dashboard.jsp");
                }
            } else {
                // Login failed
                request.setAttribute("errorMessage", "Invalid email or password.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
}
