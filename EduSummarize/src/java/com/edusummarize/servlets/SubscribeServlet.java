package com.edusummarize.servlets;

import com.edusummarize.utils.DatabaseUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * SubscribeServlet - Manages Premium Subscriptions
 * 
 * CLASS DIAGRAM RELATIONSHIPS:
 * ============================
 * 
 * 1. User → Subscription (Directed Association 1 to 0..1)
 *    - Each User can have zero or one active Subscription
 *    - Multiplicity: 1 User to 0..1 Subscription
 *    - Direction: User navigates to Subscription (User knows about Subscription)
 * 
 * Subscription Attributes:
 * - subscriptionID: int (represented by user's is_premium status)
 * - userID: String (foreign key to User)
 * - planType: String (Free/Premium - RM50/month)
 * - startDate: Date (subscription_start)
 * - endDate: Date (subscription_end)
 * - paymentStatus: String (active/expired)
 * 
 * Subscription Operations:
 * - activatePlan(): Sets is_premium=1 and subscription dates
 * - cancelPlan(): Would set is_premium=0 (not implemented yet)
 * 
 * 2. Subscription → Payment (Directed Association 1 to 1..*)
 *    - Each Subscription has one or more Payment records
 *    - Multiplicity: 1 Subscription to 1..* Payments
 *    - Direction: Subscription navigates to Payment history
 * 
 * Payment Attributes:
 * - paymentID: String
 * - subscriptionID: String
 * - userID: String
 * - amount: double (RM50.00 for premium)
 * - paymentDate: Date
 * - transactionStatus: String (completed/pending/failed)
 * 
 * Payment Operations:
 * - processPayment(): Handles payment transaction (simplified in this version)
 * 
 * BUSINESS LOGIC:
 * - Free users: 100 PDF limit, unlimited text input
 * - Premium users: Unlimited PDFs, RM50/month subscription
 */
public class SubscribeServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        // Check if user is logged in
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Get payment information (in production, integrate with real payment gateway)
        String cardName = request.getParameter("cardName");
        String cardNumber = request.getParameter("cardNumber");
        String expiry = request.getParameter("expiry");
        String cvv = request.getParameter("cvv");
        
        // Validate input
        if (cardName == null || cardName.trim().isEmpty() ||
            cardNumber == null || cardNumber.trim().isEmpty() ||
            expiry == null || expiry.trim().isEmpty() ||
            cvv == null || cvv.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please fill in all payment information.");
            request.getRequestDispatcher("subscribe.jsp").forward(request, response);
            return;
        }
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            // Calculate subscription dates
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endDate = now.plusMonths(1);
            
            // CLASS DIAGRAM: Subscription → Payment (Directed Association, 1 to 1..*)
            // Update user to premium status
            String sql = "UPDATE users SET is_premium = 1, subscription_start = ?, subscription_end = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, Timestamp.valueOf(now));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            stmt.setInt(3, userId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Update session
                session.setAttribute("isPremium", true);
                
                // Redirect to dashboard with success message
                session.setAttribute("successMessage", "Welcome to Premium! You now have unlimited PDF summarizations.");
                response.sendRedirect("dashboard.jsp");
            } else {
                request.setAttribute("errorMessage", "Failed to process subscription. Please try again.");
                request.getRequestDispatcher("subscribe.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("subscribe.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("subscribe.jsp");
    }
}
