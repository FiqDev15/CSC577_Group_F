package com.edusummarize.servlets;

import com.edusummarize.models.Admin;
import com.edusummarize.utils.DatabaseUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * AdminServlet - Handles Admin Operations
 * Implements the 3 CLASS DIAGRAM operations:
 * 1. monitorSystemPerformance()
 * 2. manageUserAccess()
 * 3. viewSubscription()
 */
public class AdminServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check if user is admin
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        Integer adminLevel = (Integer) session.getAttribute("adminLevel");
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (isAdmin == null || !isAdmin || adminLevel == null || adminLevel == 0) {
            response.sendRedirect("dashboard.jsp?error=admin_only");
            return;
        }
        
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendRedirect("admin_dashboard.jsp");
            return;
        }
        
        // Create Admin object
        Admin admin = new Admin();
        admin.setUserID(userId);
        admin.setAdminLevel(adminLevel);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            switch (action) {
                case "monitorSystemPerformance":
                    // CLASS DIAGRAM OPERATION: monitorSystemPerformance()
                    handleMonitorSystemPerformance(admin, response);
                    break;
                    
                case "viewSubscription":
                    // CLASS DIAGRAM OPERATION: viewSubscription()
                    handleViewSubscription(admin, response);
                    break;
                    
                case "getAllUsers":
                    // Helper method to get user list
                    handleGetAllUsers(admin, response);
                    break;
                    
                default:
                    response.getWriter().write("{\"error\": \"Invalid action\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check if user is admin
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        Integer adminLevel = (Integer) session.getAttribute("adminLevel");
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (isAdmin == null || !isAdmin || adminLevel == null || adminLevel < 2) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\": \"Insufficient permissions\"}");
            return;
        }
        
        String action = request.getParameter("action");
        
        // Create Admin object
        Admin admin = new Admin();
        admin.setUserID(userId);
        admin.setAdminLevel(adminLevel);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            if ("manageUserAccess".equals(action)) {
                // CLASS DIAGRAM OPERATION: manageUserAccess()
                handleManageUserAccess(admin, request, response);
            } else if ("manageUserSubscription".equals(action)) {
                // CLASS DIAGRAM OPERATION: manageUserSubscription()
                handleManageUserSubscription(admin, request, response);
            } else {
                response.getWriter().write("{\"error\": \"Invalid action\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
    
    /**
     * CLASS DIAGRAM OPERATION: Admin.monitorSystemPerformance()
     * Returns system statistics as JSON
     */
    private void handleMonitorSystemPerformance(Admin admin, HttpServletResponse response) 
            throws IOException {
        
        Map<String, Object> metrics = admin.monitorSystemPerformance();
        
        JSONObject json = new JSONObject();
        json.put("totalUsers", metrics.get("totalUsers"));
        json.put("premiumUsers", metrics.get("premiumUsers"));
        json.put("freeUsers", metrics.get("freeUsers"));
        json.put("totalSummaries", metrics.get("totalSummaries"));
        json.put("monthlyRevenue", metrics.get("monthlyRevenue"));
        json.put("status", metrics.get("status"));
        
        response.getWriter().write(json.toString());
    }
    
    /**
     * CLASS DIAGRAM OPERATION: Admin.viewSubscription()
     * Returns all subscription details as JSON
     */
    private void handleViewSubscription(Admin admin, HttpServletResponse response) 
            throws IOException {
        
        List<Map<String, Object>> subscriptions = admin.viewSubscription();
        
        JSONArray jsonArray = new JSONArray();
        for (Map<String, Object> sub : subscriptions) {
            JSONObject json = new JSONObject();
            json.put("userID", sub.get("userID"));
            json.put("userName", sub.get("userName"));
            json.put("email", sub.get("email"));
            json.put("planType", sub.get("planType"));
            json.put("pdfCount", sub.get("pdfCount"));
            json.put("startDate", sub.get("startDate"));
            json.put("endDate", sub.get("endDate"));
            json.put("joinedDate", sub.get("joinedDate"));
            jsonArray.put(json);
        }
        
        response.getWriter().write(jsonArray.toString());
    }
    
    /**
     * CLASS DIAGRAM OPERATION: Admin.manageUserAccess()
     * Activates or deactivates user accounts
     */
    private void handleManageUserAccess(Admin admin, HttpServletRequest request, 
                                       HttpServletResponse response) throws IOException {
        
        String userIdStr = request.getParameter("userId");
        String activateStr = request.getParameter("activate");
        
        if (userIdStr == null || activateStr == null) {
            response.getWriter().write("{\"error\": \"Missing parameters\"}");
            return;
        }
        
        int targetUserId = Integer.parseInt(userIdStr);
        boolean activate = Boolean.parseBoolean(activateStr);
        
        boolean success = admin.manageUserAccess(targetUserId, activate);
        
        JSONObject json = new JSONObject();
        json.put("success", success);
        json.put("message", success ? 
            "User " + (activate ? "activated" : "deactivated") + " successfully" :
            "Failed to update user access");
        
        response.getWriter().write(json.toString());
    }
    
    /**
     * CLASS DIAGRAM OPERATION: Admin.manageUserSubscription()
     * Activates or deactivates user subscriptions (Premium/Free)
     */
    private void handleManageUserSubscription(Admin admin, HttpServletRequest request, 
                                             HttpServletResponse response) throws IOException {
        
        String userIdStr = request.getParameter("userId");
        String activateStr = request.getParameter("activate");
        
        if (userIdStr == null || activateStr == null) {
            response.getWriter().write("{\"error\": \"Missing parameters\"}");
            return;
        }
        
        int targetUserId = Integer.parseInt(userIdStr);
        boolean activate = Boolean.parseBoolean(activateStr);
        
        boolean success = admin.manageUserSubscription(targetUserId, activate);
        
        JSONObject json = new JSONObject();
        json.put("success", success);
        json.put("message", success ? 
            "User subscription " + (activate ? "activated (Premium)" : "deactivated (Free)") + " successfully" :
            "Failed to update user subscription");
        
        response.getWriter().write(json.toString());
    }
    
    /**
     * Helper method: Get all users
     */
    private void handleGetAllUsers(Admin admin, HttpServletResponse response) 
            throws IOException {
        
        List<com.edusummarize.models.User> users = admin.getAllUsers();
        
        JSONArray jsonArray = new JSONArray();
        for (com.edusummarize.models.User user : users) {
            JSONObject json = new JSONObject();
            json.put("userID", user.getUserID());
            json.put("name", user.getName());
            json.put("email", user.getEmail());
            json.put("subscriptionStatus", user.getSubscriptionStatus());
            json.put("accountStatus", user.isAccountStatus());
            json.put("pdfCount", user.getPdfCount());
            jsonArray.put(json);
        }
        
        response.getWriter().write(jsonArray.toString());
    }
}
