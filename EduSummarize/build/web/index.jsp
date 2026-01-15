<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EduSummarize - AI-Powered Lecture Summarization</title>
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
                <a href="index.jsp" class="nav-link">Home</a>
                <a href="#features" class="nav-link">Features</a>
                <% if (session.getAttribute("userId") != null) { %>
                    <a href="dashboard.jsp" class="nav-link">Dashboard</a>
                    <a href="MySummariesServlet" class="nav-link">My Summaries</a>
                    <span class="nav-link">Welcome, <%= session.getAttribute("userName") %>!</span>
                    <a href="LogoutServlet" class="nav-link">Logout</a>
                <% } else { %>
                    <a href="login.jsp" class="nav-link">Login</a>
                    <a href="register.jsp" class="nav-link">Register</a>
                <% } %>
            </nav>
        </div>
    </header>

    <div class="container">
        <!-- Main Content -->
        <main class="main-content">
            <div class="welcome-section">
                <h2>Welcome to EduSummarize</h2>
                <p>Transform lengthy lecture notes and educational materials into concise, easy-to-understand summaries using AI technology.</p>
            </div>

            <!-- Upload Form Section -->
            <div class="upload-section">
                <h3>Summarize Your Content</h3>
                <p class="instruction">Paste your text or upload a file to get started</p>
                
                <form action="SummarizeServlet" method="post" enctype="multipart/form-data" class="upload-form">
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
                </form>
            </div>

            <!-- Features Section -->
            <div class="features-section" id="features">
                <h3>Why Choose EduSummarize</h3>
                <div class="features-grid">
                    <div class="feature-card">
                        <h4>Lightning Fast</h4>
                        <p>Get summaries in seconds, not hours</p>
                    </div>
                    <div class="feature-card">
                        <h4>Accurate Extraction</h4>
                        <p>AI identifies key concepts automatically</p>
                    </div>
                    <div class="feature-card">
                        <h4>Easy Download</h4>
                        <p>Save summaries for offline study</p>
                    </div>
                    <div class="feature-card">
                        <h4>Secure & Private</h4>
                        <p>Your documents are not stored permanently</p>
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
        // Display selected file name
        document.getElementById('fileInput').addEventListener('change', function(e) {
            const fileName = e.target.files[0] ? e.target.files[0].name : '';
            document.getElementById('fileName').textContent = fileName ? 'Selected: ' + fileName : '';
        });
    </script>
</body>
</html>
