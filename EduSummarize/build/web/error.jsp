<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - EduSummarize</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <!-- Header Section -->
    <header class="header">
        <div class="header-container">
            <div class="logo">
                <h1>EduSummarize</h1>
            </div>
            <nav class="navbar">
                <% if (session.getAttribute("userId") != null) { %>
                    <a href="dashboard.jsp" class="nav-link">Dashboard</a>
                    <a href="MySummariesServlet" class="nav-link">My Summaries</a>
                    <a href="pricing.jsp" class="nav-link">Pricing</a>
                    <a href="faq.jsp" class="nav-link">FAQ</a>
                    <span class="nav-link">Welcome, <%= session.getAttribute("userName") %>!</span>
                    <a href="LogoutServlet" class="nav-link">Logout</a>
                <% } else { %>
                    <a href="login.jsp" class="nav-link">Login</a>
                    <a href="register.jsp" class="nav-link">Register</a>
                    <a href="pricing.jsp" class="nav-link">Pricing</a>
                    <a href="faq.jsp" class="nav-link">FAQ</a>
                <% } %>
            </nav>
        </div>
    </header>

    <div class="container">
        <!-- Main Content -->
        <main class="main-content">
            <div class="error-section">
                <h2>Something Went Wrong</h2>
                
                <div class="error-box">
                    <h3>Error Details</h3>
                    <p class="error-message">
                        <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "An unexpected error occurred while processing your request." %>
                    </p>
                </div>

                <div class="help-section">
                    <h3>Possible Solutions</h3>
                    <ul class="help-list">
                        <li>Make sure you uploaded a valid PDF or TXT file</li>
                        <li>Ensure the file is not corrupted or password-protected</li>
                        <li>Check that the file size is not too large (max 10MB recommended)</li>
                        <li>Verify that the file contains readable text content</li>
                        <li>Try refreshing the page and uploading again</li>
                    </ul>
                </div>

                <div class="action-buttons">
                    <% if (session.getAttribute("userId") != null) { %>
                        <a href="dashboard.jsp" class="action-btn primary">
                            Return to Dashboard
                        </a>
                    <% } else { %>
                        <a href="login.jsp" class="action-btn primary">
                            Return to Login
                        </a>
                    <% } %>
                    <button onclick="history.back()" class="action-btn secondary">
                        Go Back
                    </button>
                </div>
            </div>
        </main>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <p>&copy; 2025 EduSummarize - Making Education More Efficient</p>
    </footer>
</body>
</html>
