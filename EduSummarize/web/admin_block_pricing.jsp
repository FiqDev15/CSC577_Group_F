<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Check if user is admin - redirect to admin dashboard
    Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
    if (isAdmin != null && isAdmin) {
        response.sendRedirect("admin_dashboard.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pricing - EduSummarize</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container" style="padding: 50px; text-align: center;">
        <h1>Access Denied</h1>
        <p>Admin users cannot access pricing page.</p>
        <p>Please use the <a href="admin_dashboard.jsp">Admin Dashboard</a> for administrative functions.</p>
    </div>
</body>
</html>
