<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Database Connection Test</title>
</head>
<body>
    <h2>Database Connection Test</h2>
    <%
        try {
            // Load driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            out.println("<p style='color:green'>✓ MySQL Driver loaded successfully!</p>");
            
            // Try connection
            String url = "jdbc:mysql://localhost:3306/edusummarize_db?useSSL=false&serverTimezone=UTC";
            Connection conn = DriverManager.getConnection(url, "root", "");
            out.println("<p style='color:green'>✓ Database connection successful!</p>");
            
            // Check tables
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW TABLES");
            out.println("<p><strong>Tables in database:</strong></p><ul>");
            while(rs.next()) {
                out.println("<li>" + rs.getString(1) + "</li>");
            }
            out.println("</ul>");
            
            conn.close();
        } catch (Exception e) {
            out.println("<p style='color:red'>✗ Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
    %>
</body>
</html>
