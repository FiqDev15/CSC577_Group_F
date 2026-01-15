<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FAQ - EduSummarize</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .faq-section {
            max-width: 900px;
            margin: 0 auto;
            padding: 20px;
        }
        
        .faq-item {
            background: #fff;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            margin-bottom: 15px;
            overflow: hidden;
            transition: all 0.3s ease;
        }
        
        .faq-item:hover {
            border-color: #000;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        
        .faq-question {
            background: #000;
            color: #fff;
            padding: 18px 20px;
            cursor: pointer;
            font-weight: 600;
            font-size: 16px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            transition: background 0.3s ease;
        }
        
        .faq-question:hover {
            background: #333;
        }
        
        .faq-question::after {
            content: '+';
            font-size: 24px;
            font-weight: 300;
            transition: transform 0.3s ease;
        }
        
        .faq-question.active::after {
            transform: rotate(45deg);
        }
        
        .faq-answer {
            padding: 0 20px;
            max-height: 0;
            overflow: hidden;
            transition: all 0.3s ease;
            background: #f9f9f9;
        }
        
        .faq-answer.active {
            padding: 20px;
            max-height: 500px;
        }
        
        .faq-answer p {
            margin: 0 0 10px 0;
            line-height: 1.6;
            color: #333;
        }
        
        .faq-answer ul {
            margin: 10px 0;
            padding-left: 25px;
        }
        
        .faq-answer li {
            margin: 8px 0;
            color: #555;
        }
        
        .faq-header {
            text-align: center;
            margin-bottom: 40px;
        }
        
        .faq-header h2 {
            font-size: 36px;
            margin-bottom: 10px;
        }
        
        .faq-header p {
            color: #666;
            font-size: 18px;
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
            <div class="faq-section">
                <div class="faq-header">
                    <h2>Frequently Asked Questions</h2>
                    <p>Everything you need to know about using EduSummarize</p>
                </div>

                <!-- User Manual Section (Expandable) -->
                <div class="faq-item" style="border: 2px solid #000; margin-bottom: 30px;">
                    <div class="faq-question" style="background: linear-gradient(135deg, #000 0%, #333 100%);">
                        📖 User Manual - Complete Guide
                    </div>
                    <div class="faq-answer" style="text-align: center;">
                        <p style="font-size: 16px; margin-bottom: 20px;">Download our comprehensive user manual to learn everything about EduSummarize</p>
                        <a href="docs/EduSummarize_User_Manual.pdf" download style="background: linear-gradient(135deg, #000 0%, #333 100%); color: white; padding: 15px 40px; text-decoration: none; border-radius: 5px; font-weight: 600; display: inline-block; transition: all 0.3s ease; border: 2px solid #000;">
                            📥 Download User Manual (PDF)
                        </a>
                        <p style="font-size: 13px; color: #666; margin-top: 15px;">Includes step-by-step instructions, screenshots, and troubleshooting tips</p>
                    </div>
                </div>

                <!-- FAQ Items -->
                <div class="faq-item">
                    <div class="faq-question">
                        What is EduSummarize?
                    </div>
                    <div class="faq-answer">
                        <p>EduSummarize is an AI-powered web application that helps students and educators transform lengthy lecture notes, articles, and educational materials into concise, easy-to-understand summaries. It uses advanced natural language processing to identify and extract key concepts automatically.</p>
                    </div>
                </div>

                <div class="faq-item">
                    <div class="faq-question">
                        How do I use EduSummarize?
                    </div>
                    <div class="faq-answer">
                        <p>Using EduSummarize is simple:</p>
                        <ul>
                            <li><strong>Step 1:</strong> Create an account and log in</li>
                            <li><strong>Step 2:</strong> Go to your dashboard</li>
                            <li><strong>Step 3:</strong> (Optional) Enter a course name to organize your summaries</li>
                            <li><strong>Step 4:</strong> Either paste your text directly or upload a PDF/TXT file</li>
                            <li><strong>Step 5:</strong> Click "Generate Summary" and watch the loading animation</li>
                            <li><strong>Step 6:</strong> Review your summary and extracted key points</li>
                            <li><strong>Step 7:</strong> Your summary is automatically saved - access it anytime from "My Summaries"</li>
                        </ul>
                    </div>
                </div>

                <div class="faq-item">
                    <div class="faq-question">
                        What file formats are supported?
                    </div>
                    <div class="faq-answer">
                        <p>EduSummarize currently supports:</p>
                        <ul>
                            <li><strong>PDF files (.pdf)</strong> - Perfect for lecture slides and textbook chapters</li>
                            <li><strong>Text files (.txt)</strong> - For plain text notes and documents</li>
                            <li><strong>Direct text input</strong> - Just copy and paste any text content</li>
                        </ul>
                    </div>
                </div>

                <div class="faq-item">
                    <div class="faq-question">
                        Is there a text length limit?
                    </div>
                    <div class="faq-answer">
                        <p>Yes, for optimal performance:</p>
                        <ul>
                            <li><strong>Minimum:</strong> 200 characters (about 2-3 sentences)</li>
                            <li><strong>Maximum:</strong> 4,000 characters for most models</li>
                            <li>If your text is longer, the system will automatically process the first 4,000 characters</li>
                        </ul>
                        <p>For very long documents, consider breaking them into sections for better results.</p>
                    </div>
                </div>

                <div class="faq-item">
                    <div class="faq-question">
                        How accurate are the summaries?
                    </div>
                    <div class="faq-answer">
                        <p>EduSummarize uses state-of-the-art AI models (BART) from Hugging Face, which are trained on millions of documents. The summaries typically:</p>
                        <ul>
                            <li>Capture 90%+ of the main concepts</li>
                            <li>Extract 5-10 key points automatically</li>
                            <li>Reduce text length by 75-85% while preserving meaning</li>
                            <li>Work best with educational and informational content</li>
                        </ul>
                    </div>
                </div>

                <div class="faq-item">
                    <div class="faq-question">
                        Are my documents and summaries stored on the server?
                    </div>
                    <div class="faq-answer">
                        <p>Your privacy and data management:</p>
                        <ul>
                            <li><strong>Original documents:</strong> Files are processed in real-time and deleted immediately after summarization - we never store your original documents</li>
                            <li><strong>Summaries:</strong> Generated summaries ARE saved to your account for easy access later</li>
                            <li><strong>My Summaries page:</strong> All your summaries are organized by course name and can be viewed, downloaded, or deleted anytime</li>
                            <li><strong>Key points:</strong> Important keywords extracted from each summary are also saved for quick reference</li>
                            <li><strong>Security:</strong> Your account information and summaries are stored securely in our encrypted database</li>
                        </ul>
                        <p>You have full control - delete any summary whenever you want from the "My Summaries" page.</p>
                    </div>
                </div>

                <div class="faq-item">
                    <div class="faq-question">
                        How long does it take to generate a summary?
                    </div>
                    <div class="faq-answer">
                        <p>Summarization speed depends on content length:</p>
                        <ul>
                            <li><strong>Short texts (< 500 words):</strong> 5-10 seconds</li>
                            <li><strong>Medium texts (500-1500 words):</strong> 10-20 seconds</li>
                            <li><strong>Long texts (1500+ words):</strong> 15-30 seconds</li>
                        </ul>
                        <p>If the API is slow, the system automatically uses a fallback method to ensure you get results.</p>
                    </div>
                </div>

                <div class="faq-item">
                    <div class="faq-question">
                        Can I download or save my summaries?
                    </div>
                    <div class="faq-answer">
                        <p>Yes! Your summaries are automatically saved and you have multiple options:</p>
                        <ul>
                            <li><strong>Automatic saving:</strong> All summaries are automatically saved to your account after generation</li>
                            <li><strong>My Summaries page:</strong> Access all your past summaries organized by course name</li>
                            <li><strong>Download as PDF:</strong> Click the "Download PDF" button on any summary to save it with keywords included</li>
                            <li><strong>View anytime:</strong> Click "View" to expand and read the full summary with key points</li>
                            <li><strong>Delete option:</strong> Remove summaries you no longer need with the delete button</li>
                            <li><strong>Copy text:</strong> You can copy the text directly from the summary page</li>
                        </ul>
                        <p>All summaries include the filename, course name, date created, and extracted keywords.</p>
                    </div>
                </div>

                <div class="faq-item">
                    <div class="faq-question">
                        What AI technology powers EduSummarize?
                    </div>
                    <div class="faq-answer">
                        <p>We use cutting-edge AI models from Hugging Face:</p>
                        <ul>
                            <li><strong>BART (default):</strong> Facebook's transformer model, excellent for general summarization</li>
                            
                        </ul>
                    </div>
                </div>

                <div class="faq-item">
                    <div class="faq-question">
                        What if I encounter an error?
                    </div>
                    <div class="faq-answer">
                        <p>Common issues and solutions:</p>
                        <ul>
                            <li><strong>"Text is too short":</strong> Make sure your content has at least 200 characters</li>
                            <li><strong>"Model is loading":</strong> Wait 30 seconds and try again</li>
                            <li><strong>"API request failed":</strong> Check your internet connection or try again later</li>
                            <li><strong>PDF not processing:</strong> Ensure the PDF contains extractable text (not just images)</li>
                        </ul>
                        <p>If problems persist, try refreshing the page or logging out and back in.</p>
                    </div>
                </div>

                <div class="faq-item">
                    <div class="faq-question">
                        Is EduSummarize free to use?
                    </div>
                    <div class="faq-answer">
                        <p>EduSummarize offers both <strong>Free</strong> and <strong>Premium</strong> plans:</p>
                        <ul>
                            <li><strong>Free Plan:</strong> Includes 100 PDF summarizations per account and unlimited text input summarization. Perfect for getting started!</li>
                            <li><strong>Premium Plan:</strong> RM50/month for unlimited PDF summarizations, priority processing, advanced analytics, and email support.</li>
                        </ul>
                        <p>We believe in making education accessible, which is why we offer a generous free tier. Upgrade to Premium anytime for unlimited access.</p>
                    </div>
                </div>
                
                <div class="faq-item">
                    <div class="faq-question">
                        What happens when I reach the 100 PDF limit on the free plan?
                    </div>
                    <div class="faq-answer">
                        <p>Once you've used all 100 PDF summarizations on the free plan, you have two options:</p>
                        <ul>
                            <li><strong>Upgrade to Premium:</strong> Get unlimited PDF summarizations for RM50/month</li>
                            <li><strong>Continue with text input:</strong> You can still use unlimited text input summarization by copying and pasting your content directly</li>
                        </ul>
                        <p>Your account will show your remaining PDF credits on the dashboard.</p>
                    </div>
                </div>
                
                <div class="faq-item">
                    <div class="faq-question">
                        How do I upgrade to Premium?
                    </div>
                    <div class="faq-answer">
                        <p>Upgrading is simple:</p>
                        <ul>
                            <li>Click on "Pricing" in the navigation menu</li>
                            <li>Click "Upgrade Now" on the Premium plan card</li>
                            <li>Enter your payment information</li>
                            <li>Start enjoying unlimited summarizations immediately!</li>
                        </ul>
                        <p>You can cancel your subscription at any time with no penalties.</p>
                    </div>
                </div>

                <div class="faq-item">
                    <div class="faq-question">
                        Can I use this for assignments or exams?
                    </div>
                    <div class="faq-answer">
                        <p>EduSummarize is designed as a <strong>study aid</strong>:</p>
                        <ul>
                            <li>✅ Use it to review lecture notes and create study guides</li>
                            <li>✅ Extract key points from textbook chapters</li>
                            <li>✅ Prepare for exams by summarizing course materials</li>
                            <li>❌ Do not submit AI-generated summaries as your own work</li>
                            <li>❌ Always cite sources and use proper academic integrity</li>
                        </ul>
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
        // FAQ Accordion functionality
        document.querySelectorAll('.faq-question').forEach(question => {
            question.addEventListener('click', function() {
                const answer = this.nextElementSibling;
                const isActive = answer.classList.contains('active');
                
                // Close all answers
                document.querySelectorAll('.faq-answer').forEach(ans => {
                    ans.classList.remove('active');
                });
                document.querySelectorAll('.faq-question').forEach(q => {
                    q.classList.remove('active');
                });
                
                // Toggle current answer
                if (!isActive) {
                    answer.classList.add('active');
                    this.classList.add('active');
                }
            });
        });
    </script>
</body>
</html>
