<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pricing - EduSummarize</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .pricing-section {
            max-width: 1200px;
            margin: 0 auto;
            padding: 40px 20px;
        }
        
        .pricing-header {
            text-align: center;
            margin-bottom: 50px;
        }
        
        .pricing-header h2 {
            font-size: 42px;
            margin-bottom: 15px;
        }
        
        .pricing-header p {
            color: #666;
            font-size: 18px;
        }
        
        .pricing-cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
            gap: 40px;
            margin-top: 40px;
        }
        
        .pricing-card {
            background: #fff;
            border: 2px solid #e0e0e0;
            border-radius: 12px;
            padding: 40px 30px;
            text-align: center;
            transition: all 0.3s ease;
            position: relative;
        }
        
        .pricing-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            border-color: #000;
        }
        
        .pricing-card.premium {
            border-color: #000;
            background: linear-gradient(135deg, #000 0%, #333 100%);
            color: #fff;
        }
        
        .pricing-card.premium:hover {
            box-shadow: 0 10px 40px rgba(0,0,0,0.3);
        }
        
        .plan-badge {
            display: inline-block;
            background: #000;
            color: #fff;
            padding: 6px 16px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            text-transform: uppercase;
            margin-bottom: 20px;
        }
        
        .pricing-card.premium .plan-badge {
            background: #fff;
            color: #000;
        }
        
        .plan-name {
            font-size: 28px;
            font-weight: 700;
            margin-bottom: 15px;
        }
        
        .plan-price {
            font-size: 48px;
            font-weight: 800;
            margin: 20px 0;
        }
        
        .plan-price .currency {
            font-size: 24px;
            vertical-align: super;
        }
        
        .plan-price .period {
            font-size: 16px;
            color: #666;
            font-weight: 400;
        }
        
        .pricing-card.premium .plan-price .period {
            color: #ccc;
        }
        
        .plan-description {
            color: #666;
            margin-bottom: 30px;
            line-height: 1.6;
        }
        
        .pricing-card.premium .plan-description {
            color: #ccc;
        }
        
        .plan-features {
            list-style: none;
            padding: 0;
            margin: 30px 0;
            text-align: left;
        }
        
        .plan-features li {
            padding: 12px 0;
            border-bottom: 1px solid #f0f0f0;
            display: flex;
            align-items: center;
        }
        
        .pricing-card.premium .plan-features li {
            border-bottom-color: #444;
        }
        
        .plan-features li:last-child {
            border-bottom: none;
        }
        
        .plan-features li::before {
            content: '✓';
            color: #000;
            font-weight: bold;
            font-size: 18px;
            margin-right: 12px;
            flex-shrink: 0;
        }
        
        .pricing-card.premium .plan-features li::before {
            color: #4CAF50;
        }
        
        .plan-features li.unavailable {
            color: #999;
        }
        
        .plan-features li.unavailable::before {
            content: '✗';
            color: #999;
        }
        
        .plan-button {
            display: inline-block;
            padding: 15px 40px;
            background: #000;
            color: #fff;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            font-size: 16px;
            margin-top: 20px;
            transition: all 0.3s ease;
            border: 2px solid #000;
        }
        
        .plan-button:hover {
            background: #333;
            transform: scale(1.05);
        }
        
        .pricing-card.premium .plan-button {
            background: #fff;
            color: #000;
            border-color: #fff;
        }
        
        .pricing-card.premium .plan-button:hover {
            background: #f0f0f0;
        }
        
        .comparison-note {
            text-align: center;
            margin-top: 60px;
            padding: 30px;
            background: #f9f9f9;
            border-radius: 8px;
        }
        
        .comparison-note h3 {
            margin-bottom: 15px;
        }
        
        .comparison-note p {
            color: #666;
            line-height: 1.8;
        }
    </style>
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
        <main class="main-content">
            <div class="pricing-section">
                <div class="pricing-header">
                    <h2>Simple, Transparent Pricing</h2>
                    <p>Choose the plan that's right for you. Upgrade anytime.</p>
                </div>

                <!-- Pricing Cards -->
                <div class="pricing-cards">
                    <!-- Free Plan -->
                    <div class="pricing-card">
                        <span class="plan-badge">Free Forever</span>
                        <h3 class="plan-name">Free Plan</h3>
                        <div class="plan-price">
                            <span class="currency">RM</span>0
                            <span class="period">/month</span>
                        </div>
                        <p class="plan-description">
                            Perfect for students getting started with AI-powered summarization
                        </p>
                        
                        <ul class="plan-features">
                            <li>100 PDF summarizations per account</li>
                            <li>Unlimited text input summarization</li>
                            <li>AI-powered key points extraction</li>
                            <li>PDF & TXT file support</li>
                            <li>Download summaries as text</li>
                            <li class="unavailable">Priority processing</li>
                            <li class="unavailable">Email support</li>
                        </ul>
                        
                        <% 
                        Boolean isPremium = (Boolean) session.getAttribute("isPremium");
                        if (session.getAttribute("userId") != null) {
                            if (isPremium != null && isPremium) { %>
                                <a href="dashboard.jsp" class="plan-button">Go to Dashboard</a>
                            <% } else { %>
                                <a href="dashboard.jsp" class="plan-button">Current Plan</a>
                            <% }
                        } else { %>
                            <a href="register.jsp" class="plan-button">Get Started Free</a>
                        <% } %>
                    </div>

                    <!-- Premium Plan -->
                    <div class="pricing-card premium">
                        <span class="plan-badge">Most Popular</span>
                        <h3 class="plan-name">Premium Plan</h3>
                        <div class="plan-price">
                            <span class="currency">RM</span>50
                            <span class="period">/month</span>
                        </div>
                        <p class="plan-description">
                            Unlimited power for serious students and educators
                        </p>
                        
                        <ul class="plan-features">
                            <li><strong>Unlimited PDF summarizations</strong></li>
                            <li>Unlimited text input summarization</li>
                            <li>AI-powered key points extraction</li>
                            <li>PDF & TXT file support</li>
                            <li>Download summaries as text</li>
                            <li><strong>Priority processing</strong> - Faster results</li>
                            <li><strong>Email support</strong> - Get help when needed</li>
                        </ul>
                        
                        <% 
                        if (session.getAttribute("userId") != null) {
                            if (isPremium != null && isPremium) { %>
                                <a href="dashboard.jsp" class="plan-button">Current Plan</a>
                            <% } else { %>
                                <a href="subscribe.jsp" class="plan-button">Upgrade Now</a>
                            <% }
                        } else { %>
                            <a href="register.jsp" class="plan-button">Start Premium Trial</a>
                        <% } %>
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
