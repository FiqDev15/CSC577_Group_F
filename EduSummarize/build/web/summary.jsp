<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Summary Results - EduSummarize</title>
    <link rel="stylesheet" href="css/style.css">
    <!-- jsPDF library for PDF generation -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
    <style>
        /* Print styles to prevent page breaks in key sections */
        @media print {
            /* Hide navigation and action buttons when printing */
            .header, .footer, .action-buttons {
                display: none !important;
            }
            
            /* Prevent page breaks inside these elements */
            .info-box, .summary-box, .keypoints-box {
                page-break-inside: avoid;
                break-inside: avoid;
            }
            
            /* Force key points to start on a new page */
            .keypoints-box {
                page-break-before: always;
                break-before: page;
                margin-top: 0;
            }
            
            /* Keep each key point together */
            .keypoints-list li {
                page-break-inside: avoid;
                break-inside: avoid;
            }
            
            /* Add some spacing */
            .summary-box {
                margin-bottom: 20px;
            }
            
            /* Ensure content fits well on page */
            body {
                margin: 20px;
            }
            
            .container {
                max-width: 100%;
            }
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
        <!-- Main Content -->
        <main class="main-content">
            <div class="results-section">
                <h2>Summary Results</h2>
                
                <!-- File Information -->
                <div class="info-box">
                    <h3>Document Information</h3>
                    <p><strong>File Name:</strong> <%= request.getAttribute("fileName") != null ? request.getAttribute("fileName") : "N/A" %></p>
                    <p><strong>File Type:</strong> <%= request.getAttribute("fileType") != null ? request.getAttribute("fileType") : "N/A" %></p>
                    <p><strong>Original Length:</strong> <%= request.getAttribute("originalLength") != null ? request.getAttribute("originalLength") : "0" %> characters</p>
                    <p><strong>Processing Time:</strong> <%= request.getAttribute("processingTime") != null ? request.getAttribute("processingTime") : "N/A" %> seconds</p>
                </div>

                <!-- Summary Section -->
                <div class="summary-box">
                    <div class="summary-header">
                        <h3>PDF Summary</h3>
                        <button onclick="copySummary()" class="copy-btn">Copy</button>
                    </div>
                    <div id="summaryContent" class="summary-content">
                        <%= request.getAttribute("summary") != null ? request.getAttribute("summary") : "No summary available." %>
                    </div>
                </div>

                <!-- Key Points Section -->
                <% 
                    List<String> keyPoints = (List<String>) request.getAttribute("keyPoints");
                    if (keyPoints != null && !keyPoints.isEmpty()) {
                %>
                <div class="keypoints-box">
                    <h3>Key Points Extracted</h3>
                    <ul class="keypoints-list">
                        <% for (String point : keyPoints) { %>
                            <li><%= point %></li>
                        <% } %>
                    </ul>
                </div>
                <% } %>

                <!-- Action Buttons -->
                <div class="action-buttons">
                    <button onclick="downloadSummary()" class="action-btn primary">
                         Download as PDF
                    </button>
                    <button onclick="printSummary()" class="action-btn secondary">
                        Print Summary
                    </button>
                    <a href="dashboard.jsp" class="action-btn tertiary">
                        Summarize Another Document
                    </a>
                </div>
            </div>
        </main>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <p>&copy; 2025 EduSummarize - Making Education More Efficient</p>
    </footer>

    <script>
        function copySummary() {
            const summaryText = document.getElementById('summaryContent').innerText;
            navigator.clipboard.writeText(summaryText).then(function() {
                alert('Summary copied to clipboard!');
            }, function(err) {
                alert('Failed to copy summary.');
            });
        }

        function downloadSummary() {
            // Generate PDF directly using jsPDF
            const { jsPDF } = window.jspdf;
            const doc = new jsPDF();
            
            // Get data
            const fileName = '<%= request.getAttribute("fileName") != null ? request.getAttribute("fileName") : "document" %>';
            const fileType = '<%= request.getAttribute("fileType") != null ? request.getAttribute("fileType") : "N/A" %>';
            const originalLength = '<%= request.getAttribute("originalLength") != null ? request.getAttribute("originalLength") : "0" %>';
            const summary = document.getElementById('summaryContent').innerText;
            
            // PDF styling
            const pageWidth = doc.internal.pageSize.getWidth();
            const margin = 15;
            const maxWidth = pageWidth - (margin * 2);
            let yPosition = 20;
            
            // Title
            doc.setFontSize(20);
            doc.setFont(undefined, 'bold');
            doc.text('EduSummarize - AI Summary', margin, yPosition);
            yPosition += 10;
            
            // Divider line
            doc.setLineWidth(0.5);
            doc.line(margin, yPosition, pageWidth - margin, yPosition);
            yPosition += 10;
            
            // Document Info
            doc.setFontSize(11);
            doc.setFont(undefined, 'bold');
            doc.text('Document Information:', margin, yPosition);
            yPosition += 7;
            
            doc.setFont(undefined, 'normal');
            doc.setFontSize(10);
            doc.text('File Name: ' + fileName, margin, yPosition);
            yPosition += 6;
            doc.text('File Type: ' + fileType, margin, yPosition);
            yPosition += 6;
            doc.text('Original Length: ' + originalLength + ' characters', margin, yPosition);
            yPosition += 6;
            doc.text('Generated: ' + new Date().toLocaleString(), margin, yPosition);
            yPosition += 12;
            
            // Summary Section
            doc.setFontSize(12);
            doc.setFont(undefined, 'bold');
            doc.text('Summary:', margin, yPosition);
            yPosition += 8;
            
            doc.setFont(undefined, 'normal');
            doc.setFontSize(10);
            const summaryLines = doc.splitTextToSize(summary, maxWidth);
            summaryLines.forEach(function(line) {
                if (yPosition > 270) { // Check if need new page
                    doc.addPage();
                    yPosition = 20;
                }
                doc.text(line, margin, yPosition);
                yPosition += 6;
            });
            
            // Key Points Section
            const keyPointsList = document.querySelector('.keypoints-list');
            if (keyPointsList) {
                yPosition += 10;
                if (yPosition > 250) { // Start on new page if not enough space
                    doc.addPage();
                    yPosition = 20;
                }
                
                doc.setFontSize(12);
                doc.setFont(undefined, 'bold');
                doc.text('Key Points:', margin, yPosition);
                yPosition += 8;
                
                doc.setFont(undefined, 'normal');
                doc.setFontSize(10);
                const points = keyPointsList.querySelectorAll('li');
                points.forEach(function(point, index) {
                    if (yPosition > 270) {
                        doc.addPage();
                        yPosition = 20;
                    }
                    const pointText = (index + 1) + '. ' + point.textContent;
                    const pointLines = doc.splitTextToSize(pointText, maxWidth - 5);
                    pointLines.forEach(function(line) {
                        if (yPosition > 270) {
                            doc.addPage();
                            yPosition = 20;
                        }
                        doc.text(line, margin + 3, yPosition);
                        yPosition += 6;
                    });
                    yPosition += 2; // Space between points
                });
            }
            
            // Save the PDF
            const pdfFileName = fileName.replace(/\.[^/.]+$/, '') + '_summary.pdf';
            doc.save(pdfFileName);
        }

        function printSummary() {
            window.print();
        }
    </script>
</body>
</html>
