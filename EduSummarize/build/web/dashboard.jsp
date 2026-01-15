<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Check if user is logged in
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    String userName = (String) session.getAttribute("userName");
    Boolean isPremium = (Boolean) session.getAttribute("isPremium");
    Integer pdfCount = (Integer) session.getAttribute("pdfCount");
    
    // NEW: Get admin status
    Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
    Integer adminLevel = (Integer) session.getAttribute("adminLevel");
    String userRole = (String) session.getAttribute("userRole");
    
    // Handle null values
    if (isPremium == null) isPremium = false;
    if (pdfCount == null) pdfCount = 0;
    if (isAdmin == null) isAdmin = false;
    if (adminLevel == null) adminLevel = 0;
    
    int pdfRemaining = isPremium ? -1 : (100 - pdfCount); // -1 means unlimited
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - EduSummarize</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .success-message {
            background: #4CAF50;
            color: #fff;
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            text-align: center;
            font-weight: 600;
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
                <% if (isAdmin != null && isAdmin) { %>
                <!-- Admin users only see Admin link -->
                <a href="admin_dashboard.jsp" class="nav-link" style="color: #ff4444; font-weight: bold;">Admin</a>
                <% } else { %>
                <!-- Regular users see Pricing and FAQ -->
                <a href="pricing.jsp" class="nav-link">Pricing</a>
                <a href="faq.jsp" class="nav-link">FAQ</a>
                <% } %>
                <span class="nav-link">Welcome, <%= userName %>!</span>
                <a href="LogoutServlet" class="nav-link">Logout</a>
            </nav>
        </div>
    </header>

    <div class="container">
        <!-- Main Content -->
        <main class="main-content">
            <!-- Success Message -->
            <% 
                String successMessage = (String) session.getAttribute("successMessage");
                if (successMessage != null && !successMessage.isEmpty()) {
                    session.removeAttribute("successMessage");
            %>
            <div class="success-message">
                <%= successMessage %>
            </div>
            <% } %>
            
            <div class="welcome-section">
                <h2>Welcome to Your Dashboard, <%= userName %>!</h2>
                <p>Transform lengthy lecture notes and educational materials into concise, easy-to-understand summaries using AI technology.</p>
            </div>

            <!-- Upload Form Section -->
            <div class="upload-section">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
                    <h3 style="margin: 0;">Summarize Your Content</h3>
                    <% if (isAdmin != null && isAdmin) { %>
                    <!-- Admin Badge -->
                    <span style="background: linear-gradient(135deg, #ff4444 0%, #cc0000 100%); color: #fff; padding: 6px 16px; border-radius: 20px; font-size: 14px; font-weight: 600; box-shadow: 0 2px 4px rgba(0,0,0,0.2);">
                        Admin (Level <%= adminLevel %>)
                    </span>
                    <% } else if (!isPremium) { %>
                    <!-- Free User Badge -->
                    <span style="background: #f0f0f0; color: #666; padding: 6px 16px; border-radius: 20px; font-size: 14px; font-weight: 600;">
                        Free User
                    </span>
                    <% } else { %>
                    <!-- Premium User Badge -->
                    <span style="background: #000; color: #fff; padding: 6px 16px; border-radius: 20px; font-size: 14px; font-weight: 600;">
                        Premium User
                    </span>
                    <% } %>
                </div>
                <p class="instruction">Paste your text or upload a file to get started</p>
                
                <form action="SummarizeServlet" method="post" enctype="multipart/form-data" class="upload-form">
                    <!-- Course Name Input -->
                    <div class="course-input-section">
                        <label for="courseName" class="course-label">Course Name:</label>
                        <input 
                            type="text" 
                            id="courseName" 
                            name="courseName" 
                            class="course-input" 
                            placeholder="">
                        <small style="color: #666; display: block; margin-top: 5px; font-size: 0.85em;">Organize your summaries by course</small>
                    </div>
                    
                    <!-- Two Column Layout -->
                    <div class="input-container">
                        <!-- Left: Text Input -->
                        <div class="text-input-column">
                            <label class="column-label">Paste Your Text</label>
                            <textarea 
                                name="textContent" 
                                class="text-area" 
                                placeholder="Paste your lecture notes, article, or any text content here..."></textarea>
                        </div>
                        
                        <!-- Right: File Upload -->
                        <div class="file-input-column">
                            <label class="column-label">Upload a File</label>
                            <label for="fileInput" class="file-label">
                                <span class="upload-text">Choose File (PDF or TXT)</span>
                            </label>
                            <input type="file" id="fileInput" name="file" accept=".pdf,.txt" class="file-input">
                            <span id="fileName" class="file-name"></span>
                        </div>
                    </div>

                    <button type="submit" class="submit-btn">
                        <span>Generate Summary</span>
                    </button>
                    
                    <% if (!isPremium) { %>
                    <div style="text-align: right; margin-top: 10px;">
                        <span style="color: #666; font-size: 14px; font-weight: 500;">
                            PDF Usage: <%= pdfCount %>/100
                        </span>
                    </div>
                    <% } else { %>
                    <div style="text-align: right; margin-top: 10px;">
                        <span style="color: #666; font-size: 14px; font-weight: 500;">
                            Premium: Unlimited PDFs
                        </span>
                    </div>
                    <% } %>
                </form>
            </div>

            <!-- Features Section -->
            <div class="features-section">
                <h3>Available Features</h3>
                <div class="features-grid">
                    <div class="feature-card">
                        <h4>Text Summarization</h4>
                        <p>Paste any text and get instant AI-powered summaries</p>
                    </div>
                    <div class="feature-card">
                        <h4>PDF Processing</h4>
                        <p>Upload PDF files and extract key information</p>
                    </div>
                    <div class="feature-card">
                        <h4>Key Points Extraction</h4>
                        <p>Automatically identify the most important concepts</p>
                    </div>
                    <div class="feature-card">
                        <h4>Lightning Fast</h4>
                        <p>Get results in seconds using advanced AI models</p>
                    </div>
                </div>
            </div>
        </main>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <p>&copy; 2025 EduSummarize - Making Education More Efficient</p>
    </footer>

    <!-- Loading Overlay -->
    <div id="loadingOverlay" class="loading-overlay">
        <div class="loading-content">
            <div class="loading-spinner"></div>
            <h2 class="loading-title">Generating Your Summary</h2>
            <p class="loading-text">AI is analyzing your content...</p>
            <div class="loading-progress">
                <div class="loading-progress-bar"></div>
            </div>
            <p class="loading-subtext">This may take a few moments</p>
        </div>
    </div>

    <script>
        // Display selected file name
        document.getElementById('fileInput').addEventListener('change', function(e) {
            const fileName = e.target.files[0] ? e.target.files[0].name : '';
            document.getElementById('fileName').textContent = fileName ? 'Selected: ' + fileName : '';
        });
        
        // Show loading overlay when form is submitted
        document.querySelector('.upload-form').addEventListener('submit', function(e) {
            // Basic validation
            const textContent = document.querySelector('[name="textContent"]').value.trim();
            const fileInput = document.getElementById('fileInput').files.length;
            
            if (!textContent && !fileInput) {
                e.preventDefault();
                alert('Please either paste text or upload a file.');
                return;
            }
            
            // Show loading overlay
            document.getElementById('loadingOverlay').classList.add('active');
        });
    </script>
</body>
</html>
