<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Check if user is logged in
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    String userName = (String) session.getAttribute("userName");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Subscribe to Premium - EduSummarize</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .subscribe-section {
            max-width: 700px;
            margin: 0 auto;
            padding: 40px 20px;
        }
        
        .subscribe-header {
            text-align: center;
            margin-bottom: 40px;
        }
        
        .subscribe-header h2 {
            font-size: 36px;
            margin-bottom: 10px;
        }
        
        .subscribe-header p {
            color: #666;
            font-size: 18px;
        }
        
        .plan-summary {
            background: linear-gradient(135deg, #000 0%, #333 100%);
            color: #fff;
            padding: 30px;
            border-radius: 12px;
            margin-bottom: 30px;
            text-align: center;
        }
        
        .plan-summary h3 {
            font-size: 28px;
            margin-bottom: 15px;
        }
        
        .plan-summary .price {
            font-size: 48px;
            font-weight: 800;
            margin: 20px 0;
        }
        
        .plan-summary .price .currency {
            font-size: 24px;
            vertical-align: super;
        }
        
        .plan-summary .price .period {
            font-size: 16px;
            color: #ccc;
            font-weight: 400;
        }
        
        .payment-form {
            background: #f9f9f9;
            padding: 40px;
            border-radius: 12px;
            border: 2px solid #e0e0e0;
        }
        
        .form-section {
            margin-bottom: 30px;
        }
        
        .form-section h4 {
            margin-bottom: 20px;
            font-size: 18px;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #333;
        }
        
        .form-input {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #ddd;
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s ease;
        }
        
        .form-input:focus {
            outline: none;
            border-color: #000;
        }
        
        .card-row {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 15px;
        }
        
        .subscribe-btn {
            width: 100%;
            padding: 18px;
            background: #000;
            color: #fff;
            border: none;
            border-radius: 8px;
            font-size: 18px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .subscribe-btn:hover {
            background: #333;
            transform: translateY(-2px);
        }
        
        .security-note {
            text-align: center;
            margin-top: 20px;
            color: #666;
            font-size: 14px;
        }
        
        .benefits-list {
            background: #fff;
            padding: 30px;
            border-radius: 12px;
            border: 2px solid #e0e0e0;
            margin-bottom: 30px;
        }
        
        .benefits-list h4 {
            margin-bottom: 20px;
            font-size: 20px;
        }
        
        .benefits-list ul {
            list-style: none;
            padding: 0;
        }
        
        .benefits-list li {
            padding: 12px 0;
            border-bottom: 1px solid #f0f0f0;
            display: flex;
            align-items: center;
        }
        
        .benefits-list li:last-child {
            border-bottom: none;
        }
        
        .benefits-list li::before {
            content: '✓';
            color: #4CAF50;
            font-weight: bold;
            font-size: 20px;
            margin-right: 12px;
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
                <a href="dashboard.jsp" class="nav-link">Dashboard</a>
                <a href="MySummariesServlet" class="nav-link">My Summaries</a>
                <a href="pricing.jsp" class="nav-link">Pricing</a>
                <a href="faq.jsp" class="nav-link">FAQ</a>
                <span class="nav-link">Welcome, <%= userName %>!</span>
                <a href="LogoutServlet" class="nav-link">Logout</a>
            </nav>
        </div>
    </header>

    <div class="container">
        <main class="main-content">
            <div class="subscribe-section">
                <div class="subscribe-header">
                    <h2>Upgrade to Premium</h2>
                    <p>Unlock unlimited summarizations and premium features</p>
                </div>

                <!-- Plan Summary -->
                <div class="plan-summary">
                    <h3>Premium Plan</h3>
                    <div class="price">
                        <span class="currency">RM</span>50
                        <span class="period">/month</span>
                    </div>
                    <p>Billed monthly • Cancel anytime</p>
                </div>

                <!-- Benefits -->
                <div class="benefits-list">
                    <h4>What You'll Get:</h4>
                    <ul>
                        <li>Unlimited PDF summarizations</li>
                        <li>Priority processing for faster results</li>
                        <li>Advanced analytics and usage tracking</li>
                        <li>Email support with 24-hour response time</li>
                        <li>Download summaries in multiple formats</li>
                        <li>No ads or distractions</li>
                    </ul>
                </div>

                <!-- Payment Form -->
                <div class="payment-form">
                    <form action="SubscribeServlet" method="post">
                        <div class="form-section">
                            <h4>Payment Information</h4>
                            
                            <div class="form-group">
                                <label for="cardName">Name on Card</label>
                                <input type="text" id="cardName" name="cardName" class="form-input" 
                                       placeholder="John Doe" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="cardNumber">Card Number</label>
                                <input type="text" id="cardNumber" name="cardNumber" class="form-input" 
                                       placeholder="1234 5678 9012 3456" maxlength="19" required>
                            </div>
                            
                            <div class="card-row">
                                <div class="form-group">
                                    <label for="expiry">Expiry Date</label>
                                    <input type="text" id="expiry" name="expiry" class="form-input" 
                                           placeholder="MM/YY" maxlength="5" required>
                                </div>
                                
                                <div class="form-group">
                                    <label for="cvv">CVV</label>
                                    <input type="text" id="cvv" name="cvv" class="form-input" 
                                           placeholder="123" maxlength="3" required>
                                </div>
                            </div>
                        </div>
                        
                        <button type="submit" class="subscribe-btn">
                            Subscribe for RM50/month
                        </button>
                        
                        <p class="security-note">
                            🔒 Your payment information is secure and encrypted. 
                            We never store your card details.
                        </p>
                    </form>
                </div>
            </div>
        </main>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <p>&copy; 2025 EduSummarize - Making Education More Efficient</p>
    </footer>

    <script>
        // Format card number
        document.getElementById('cardNumber').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\s/g, '');
            let formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;
            e.target.value = formattedValue;
        });
        
        // Format expiry date
        document.getElementById('expiry').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length >= 2) {
                value = value.slice(0, 2) + '/' + value.slice(2, 4);
            }
            e.target.value = value;
        });
        
        // CVV numbers only
        document.getElementById('cvv').addEventListener('input', function(e) {
            e.target.value = e.target.value.replace(/\D/g, '');
        });
    </script>
</body>
</html>
