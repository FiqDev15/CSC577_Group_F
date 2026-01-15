<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - EduSummarize</title>
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
                <h2>Create Your Account</h2>
                <p class="auth-description">Join EduSummarize and start summarizing smarter</p>

                <!-- Error Message -->
                <% 
                    String errorMessage = (String) request.getAttribute("errorMessage");
                    if (errorMessage != null && !errorMessage.isEmpty()) {
                %>
                <div class="error-message-box">
                    <%= errorMessage %>
                </div>
                <% } %>

                <!-- Register Form -->
                <div class="auth-form-container">
                    <form action="RegisterServlet" method="post" class="auth-form">
                        <div class="form-group">
                            <label for="fullName">Full Name</label>
                            <input type="text" id="fullName" name="fullName" class="form-input" 
                                   placeholder="Enter your full name" required>
                        </div>

                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" class="form-input" 
                                   placeholder="Enter your email" required>
                        </div>

                        <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" id="password" name="password" class="form-input" 
                                   placeholder="Create a password (min 6 characters)" required minlength="6">
                        </div>

                        <div class="form-group">
                            <label for="confirmPassword">Confirm Password</label>
                            <input type="password" id="confirmPassword" name="confirmPassword" class="form-input" 
                                   placeholder="Confirm your password" required minlength="6">
                        </div>

                        <button type="submit" class="submit-btn">
                            <span>Register</span>
                        </button>
                    </form>

                    <div class="auth-footer">
                        <p>Already have an account? <a href="login.jsp" class="auth-link">Login here</a></p>
                    </div>
                </div>
            </div>
        </main>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <p>&copy; 2025 EduSummarize - Making Education More Efficient</p>
    </footer>

    <script>
        // Client-side password validation
        document.querySelector('.auth-form').addEventListener('submit', function(e) {
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (password !== confirmPassword) {
                e.preventDefault();
                alert('Passwords do not match!');
            }
        });
    </script>
</body>
</html>
