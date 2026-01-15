<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - EduSummarize</title>
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
                <a href="login.jsp" class="nav-link">Login</a>
                <a href="register.jsp" class="nav-link">Register</a>
                <a href="pricing.jsp" class="nav-link">Pricing</a>
                <a href="faq.jsp" class="nav-link">FAQ</a>
            </nav>
        </div>
    </header>

    <div class="container">
        <!-- Main Content -->
        <main class="main-content">
            <div class="auth-section">
                <h2>Login to Your Account</h2>
                <p class="auth-description">Access your personalized summarization dashboard</p>

                <!-- Error Message -->
                <% 
                    String errorMessage = (String) request.getAttribute("errorMessage");
                    if (errorMessage != null && !errorMessage.isEmpty()) {
                %>
                <div class="error-message-box">
                    <%= errorMessage %>
                </div>
                <% } %>

                <!-- Success Message -->
                <% 
                    String successMessage = (String) request.getAttribute("successMessage");
                    if (successMessage != null && !successMessage.isEmpty()) {
                %>
                <div class="success-message-box">
                    <%= successMessage %>
                </div>
                <% } %>

                <!-- Login Form -->
                <div class="auth-form-container">
                    <form action="LoginServlet" method="post" class="auth-form">
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" class="form-input" 
                                   placeholder="Enter your email" required>
                        </div>

                        <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" id="password" name="password" class="form-input" 
                                   placeholder="Enter your password" required>
                        </div>

                        <button type="submit" class="submit-btn">
                            <span>Login</span>
                        </button>
                    </form>

                    <div class="auth-footer">
                        <p>Don't have an account? <a href="register.jsp" class="auth-link">Register here</a></p>
                    </div>
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
