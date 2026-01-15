<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    // Check if user is logged in
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    String userName = (String) session.getAttribute("userName");
    Map<String, List<Map<String, Object>>> summariesByCourse = 
        (Map<String, List<Map<String, Object>>>) request.getAttribute("summariesByCourse");
    List<String> allCourses = (List<String>) request.getAttribute("allCourses");
    String filterCourse = (String) request.getAttribute("filterCourse");
    
    String successMessage = (String) session.getAttribute("successMessage");
    String errorMessage = (String) session.getAttribute("errorMessage");
    session.removeAttribute("successMessage");
    session.removeAttribute("errorMessage");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Summaries - EduSummarize</title>
    <link rel="stylesheet" href="css/style.css">
    <!-- jsPDF library for PDF generation -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
    <style>
        body {
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }
        
        .summaries-container {
            max-width: 1100px;
            margin: 0 auto;
            padding: 40px 20px;
            flex: 1;
        }
        
        .page-header {
            margin-bottom: 40px;
        }
        
        .page-header h2 {
            font-size: 2em;
            margin-bottom: 10px;
        }
        
        .filter-section {
            background: #fafafa;
            padding: 20px;
            border: 2px solid #e0e0e0;
            margin-bottom: 30px;
            display: flex;
            gap: 15px;
            align-items: center;
            flex-wrap: wrap;
        }
        
        .filter-section label {
            font-weight: 600;
        }
        
        .filter-section select {
            padding: 10px 15px;
            border: 2px solid #e0e0e0;
            background: white;
            font-size: 1em;
            cursor: pointer;
        }
        
        .filter-section button {
            padding: 10px 20px;
            background: #1a1a1a;
            color: white;
            border: none;
            cursor: pointer;
            font-weight: 600;
        }
        
        .filter-section button:hover {
            background: #333;
        }
        
        .course-section {
            margin-bottom: 50px;
            background: white;
            border: 2px solid #e0e0e0;
            padding: 30px;
        }
        
        .course-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 2px solid #e0e0e0;
        }
        
        .course-header h3 {
            font-size: 1.5em;
            color: #1a1a1a;
        }
        
        .course-count {
            background: #1a1a1a;
            color: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 0.9em;
        }
        
        .summary-card {
            background: #fafafa;
            border: 2px solid #e0e0e0;
            padding: 25px;
            margin-bottom: 20px;
            transition: all 0.2s ease;
        }
        
        .summary-card:hover {
            border-color: #1a1a1a;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        
        .summary-card-header {
            display: flex;
            justify-content: space-between;
            align-items: start;
            margin-bottom: 15px;
            padding-bottom: 15px;
            border-bottom: 2px solid #e0e0e0;
            gap: 20px;
            flex-wrap: wrap;
        }
        
        .summary-info {
            flex: 1;
            min-width: 300px;
            word-wrap: break-word;
            overflow-wrap: break-word;
        }
        
        .summary-title {
            font-size: 1.2em;
            font-weight: 600;
            color: #1a1a1a;
            margin-bottom: 8px;
        }
        
        .summary-meta {
            color: #666;
            font-size: 0.9em;
            line-height: 1.6;
        }
        
        .summary-actions {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            flex-shrink: 0;
        }
        
        .btn-action {
            padding: 10px 20px;
            border: none;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.2s ease;
            font-size: 0.9em;
        }
        
        .btn-view {
            background: #1a1a1a;
            color: white;
        }
        
        .btn-view:hover {
            background: #333;
        }
        
        .btn-download {
            background: #0066cc;
            color: white;
        }
        
        .btn-download:hover {
            background: #0052a3;
        }
        
        .btn-delete {
            background: #ff4444;
            color: white;
        }
        
        .btn-delete:hover {
            background: #cc0000;
        }
        
        .summary-content-section {
            display: none;
            margin-top: 20px;
            padding-top: 20px;
            border-top: 2px solid #e0e0e0;
        }
        
        .summary-content-section.show {
            display: block;
        }
        
        .summary-box {
            background: white;
            border: 2px solid #e0e0e0;
            padding: 20px;
            margin-bottom: 20px;
        }
        
        .summary-box h4 {
            margin-bottom: 15px;
            color: #1a1a1a;
        }
        
        .summary-text {
            line-height: 1.8;
            color: #333;
        }
        
        .keypoints-list {
            list-style: none;
            padding-left: 0;
        }
        
        .keypoints-list li {
            padding: 10px 0 10px 30px;
            position: relative;
            line-height: 1.6;
            color: #333;
        }
        
        .keypoints-list li:before {
            content: "•";
            position: absolute;
            left: 10px;
            font-size: 1.5em;
            color: #1a1a1a;
        }
        
        .no-summaries {
            text-align: center;
            padding: 60px 20px;
            color: #666;
        }
        
        .no-summaries h3 {
            margin-bottom: 15px;
        }
        
        .message {
            padding: 15px 20px;
            margin-bottom: 20px;
            border: 2px solid;
        }
        
        .message.success {
            background: #d4edda;
            border-color: #28a745;
            color: #155724;
        }
        
        .message.error {
            background: #f8d7da;
            border-color: #dc3545;
            color: #721c24;
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

    <div class="summaries-container">
        <div class="page-header">
            <h2>My Summaries</h2>
            <p>Your summaries organized by course</p>
        </div>
        
        <% if (successMessage != null) { %>
            <div class="message success"><%= successMessage %></div>
        <% } %>
        
        <% if (errorMessage != null) { %>
            <div class="message error"><%= errorMessage %></div>
        <% } %>
        
        <!-- Filter Section -->
        <% if (allCourses != null && !allCourses.isEmpty()) { %>
        <div class="filter-section">
            <label>Filter by Course:</label>
            <select id="courseFilter">
                <option value="">All Courses</option>
                <% for (String course : allCourses) { %>
                    <option value="<%= course %>" <%= course.equals(filterCourse) ? "selected" : "" %>>
                        <%= course %>
                    </option>
                <% } %>
            </select>
            <button onclick="filterCourse()">Apply Filter</button>
            <% if (filterCourse != null) { %>
                <button onclick="clearFilter()">Clear Filter</button>
            <% } %>
        </div>
        <% } %>
        
        <!-- Summaries by Course -->
        <% if (summariesByCourse != null && !summariesByCourse.isEmpty()) { %>
            <% SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a"); %>
            <% for (Map.Entry<String, List<Map<String, Object>>> entry : summariesByCourse.entrySet()) { %>
                <% String courseName = entry.getKey(); %>
                <% List<Map<String, Object>> summaries = entry.getValue(); %>
                
                <div class="course-section">
                    <div class="course-header">
                        <h3><%= courseName %></h3>
                        <span class="course-count"><%= summaries.size() %> <%= summaries.size() == 1 ? "Summary" : "Summaries" %></span>
                    </div>
                    
                    <% for (Map<String, Object> summary : summaries) { %>
                        <% List<String> keyPoints = (List<String>) summary.get("keyPoints"); %>
                        <div class="summary-card">
                            <div class="summary-card-header">
                                <div class="summary-info">
                                    <div class="summary-title">
                                        📄 <%= summary.get("fileName") %>
                                    </div>
                                    <div class="summary-meta">
                                        <strong><%= summary.get("fileType") %></strong> • 
                                        <%= summary.get("originalLength") %> characters<br>
                                        <%= sdf.format(summary.get("createdAt")) %>
                                    </div>
                                </div>
                                <div class="summary-actions">
                                    <button class="btn-action btn-view" onclick="toggleContent(<%= summary.get("id") %>)">
                                        <span id="btn-text-<%= summary.get("id") %>">View</span>
                                    </button>
                                    <button class="btn-action btn-download" onclick="downloadSummary(<%= summary.get("id") %>, '<%= summary.get("fileName") %>', '<%= summary.get("fileType") %>', <%= summary.get("originalLength") %>)">
                                        Download PDF
                                    </button>
                                    <button class="btn-action btn-delete" onclick="deleteSummary(<%= summary.get("id") %>, '<%= summary.get("fileName") %>')">
                                        Delete
                                    </button>
                                </div>
                            </div>
                            
                            <div class="summary-content-section" id="content-<%= summary.get("id") %>">
                                <div class="summary-box">
                                    <h4>Summary</h4>
                                    <div class="summary-text" id="summary-<%= summary.get("id") %>">
                                        <%= summary.get("summaryText") %>
                                    </div>
                                </div>
                                
                                <% if (keyPoints != null && !keyPoints.isEmpty()) { %>
                                <div class="summary-box">
                                    <h4>Key Points</h4>
                                    <ul class="keypoints-list" id="keypoints-<%= summary.get("id") %>">
                                        <% for (String point : keyPoints) { %>
                                            <li><%= point %></li>
                                        <% } %>
                                    </ul>
                                </div>
                                <% } %>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } %>
        <% } else { %>
            <div class="no-summaries">
                <h3>No summaries yet</h3>
                <p>Start creating summaries to see them organized by course!</p>
                <br>
                <a href="dashboard.jsp" class="submit-btn" style="display: inline-block; text-decoration: none; padding: 15px 30px;">Create Summary</a>
            </div>
        <% } %>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <p>&copy; 2025 EduSummarize - Making Education More Efficient</p>
    </footer>

    <script>
        function toggleContent(summaryId) {
            const content = document.getElementById('content-' + summaryId);
            const btnText = document.getElementById('btn-text-' + summaryId);
            
            if (content.classList.contains('show')) {
                content.classList.remove('show');
                btnText.textContent = 'View';
            } else {
                content.classList.add('show');
                btnText.textContent = 'Hide';
            }
        }
        
        function downloadSummary(summaryId, fileName, fileType, originalLength) {
            const { jsPDF } = window.jspdf;
            const doc = new jsPDF();
            
            // Get data
            const summary = document.getElementById('summary-' + summaryId).innerText;
            const keyPointsList = document.getElementById('keypoints-' + summaryId);
            
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
                if (yPosition > 270) {
                    doc.addPage();
                    yPosition = 20;
                }
                doc.text(line, margin, yPosition);
                yPosition += 6;
            });
            
            // Key Points Section
            if (keyPointsList) {
                yPosition += 10;
                if (yPosition > 250) {
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
                    yPosition += 2;
                });
            }
            
            // Save the PDF
            const pdfFileName = fileName.replace(/\.[^/.]+$/, '') + '_summary.pdf';
            doc.save(pdfFileName);
        }
        
        function filterCourse() {
            const course = document.getElementById('courseFilter').value;
            if (course) {
                window.location.href = 'MySummariesServlet?course=' + encodeURIComponent(course);
            } else {
                window.location.href = 'MySummariesServlet';
            }
        }
        
        function clearFilter() {
            window.location.href = 'MySummariesServlet';
        }
        
        function deleteSummary(summaryId, fileName) {
            if (confirm('Are you sure you want to delete the summary for "' + fileName + '"?')) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = 'MySummariesServlet';
                
                const actionInput = document.createElement('input');
                actionInput.type = 'hidden';
                actionInput.name = 'action';
                actionInput.value = 'delete';
                form.appendChild(actionInput);
                
                const summaryInput = document.createElement('input');
                summaryInput.type = 'hidden';
                summaryInput.name = 'summaryId';
                summaryInput.value = summaryId;
                form.appendChild(summaryInput);
                
                document.body.appendChild(form);
                form.submit();
            }
        }
    </script>
</body>
</html>
