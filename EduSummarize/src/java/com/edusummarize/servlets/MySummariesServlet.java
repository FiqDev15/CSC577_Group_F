package com.edusummarize.servlets;

import com.edusummarize.utils.DatabaseUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * MySummariesServlet - Displays user's saved summaries organized by course
 */
public class MySummariesServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        // Check if user is logged in
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Get filter parameter (optional)
        String filterCourse = request.getParameter("course");
        
        try {
            Connection conn = DatabaseUtil.getConnection();
            
            // Get all summaries for this user, ordered by course and date
            String sql;
            PreparedStatement stmt;
            
            if (filterCourse != null && !filterCourse.trim().isEmpty()) {
                sql = "SELECT id, file_name, file_type, course_name, original_length, " +
                      "summary_text, key_points, created_at " +
                      "FROM summaries " +
                      "WHERE user_id = ? AND course_name = ? " +
                      "ORDER BY created_at DESC";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, filterCourse);
            } else {
                sql = "SELECT id, file_name, file_type, course_name, original_length, " +
                      "summary_text, key_points, created_at " +
                      "FROM summaries " +
                      "WHERE user_id = ? " +
                      "ORDER BY course_name, created_at DESC";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            // Organize summaries by course
            Map<String, List<Map<String, Object>>> summariesByCourse = new LinkedHashMap<>();
            
            while (rs.next()) {
                String courseName = rs.getString("course_name");
                if (courseName == null || courseName.trim().isEmpty()) {
                    courseName = "Uncategorized";
                }
                
                Map<String, Object> summary = new HashMap<>();
                summary.put("id", rs.getInt("id"));
                summary.put("fileName", rs.getString("file_name"));
                summary.put("fileType", rs.getString("file_type"));
                summary.put("courseName", courseName);
                summary.put("originalLength", rs.getInt("original_length"));
                summary.put("summaryText", rs.getString("summary_text"));
                summary.put("createdAt", rs.getTimestamp("created_at"));
                
                // Parse key points from database (stored as "|||" separated string)
                String keyPointsStr = rs.getString("key_points");
                List<String> keyPointsList = new ArrayList<>();
                if (keyPointsStr != null && !keyPointsStr.trim().isEmpty()) {
                    String[] points = keyPointsStr.split("\\|\\|\\|");
                    for (String point : points) {
                        if (!point.trim().isEmpty()) {
                            keyPointsList.add(point.trim());
                        }
                    }
                }
                summary.put("keyPoints", keyPointsList);
                
                // Add to course group
                if (!summariesByCourse.containsKey(courseName)) {
                    summariesByCourse.put(courseName, new ArrayList<>());
                }
                summariesByCourse.get(courseName).add(summary);
            }
            
            // Get list of all unique courses for filter dropdown
            String coursesSql = "SELECT DISTINCT course_name FROM summaries " +
                              "WHERE user_id = ? AND course_name IS NOT NULL " +
                              "ORDER BY course_name";
            PreparedStatement coursesStmt = conn.prepareStatement(coursesSql);
            coursesStmt.setInt(1, userId);
            ResultSet coursesRs = coursesStmt.executeQuery();
            
            List<String> allCourses = new ArrayList<>();
            while (coursesRs.next()) {
                String course = coursesRs.getString("course_name");
                if (course != null && !course.trim().isEmpty()) {
                    allCourses.add(course);
                }
            }
            
            coursesRs.close();
            coursesStmt.close();
            rs.close();
            stmt.close();
            conn.close();
            
            // Set attributes for JSP
            request.setAttribute("summariesByCourse", summariesByCourse);
            request.setAttribute("allCourses", allCourses);
            request.setAttribute("filterCourse", filterCourse);
            
            // Forward to JSP
            request.getRequestDispatcher("my_summaries.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading summaries: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("delete".equals(action)) {
            deleteSummary(request, response);
        } else {
            doGet(request, response);
        }
    }
    
    private void deleteSummary(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String summaryIdStr = request.getParameter("summaryId");
        
        try {
            int summaryId = Integer.parseInt(summaryIdStr);
            
            Connection conn = DatabaseUtil.getConnection();
            
            // Delete summary (only if it belongs to this user)
            String sql = "DELETE FROM summaries WHERE id = ? AND user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, summaryId);
            stmt.setInt(2, userId);
            
            int deleted = stmt.executeUpdate();
            
            stmt.close();
            conn.close();
            
            if (deleted > 0) {
                session.setAttribute("successMessage", "Summary deleted successfully");
            } else {
                session.setAttribute("errorMessage", "Could not delete summary");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error deleting summary: " + e.getMessage());
        }
        
        // Redirect back to summaries page
        response.sendRedirect("MySummariesServlet");
    }
    
    @Override
    public String getServletInfo() {
        return "Servlet for displaying user's saved summaries organized by course";
    }
}
