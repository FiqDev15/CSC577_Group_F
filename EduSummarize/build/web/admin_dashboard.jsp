<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Check if user is logged in
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // Check if user is admin
    Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
    Integer adminLevel = (Integer) session.getAttribute("adminLevel");
    String userRole = (String) session.getAttribute("userRole");
    
    if (isAdmin == null || !isAdmin || adminLevel == null || adminLevel == 0) {
        // Not an admin - redirect to regular dashboard
        response.sendRedirect("dashboard.jsp?error=admin_only");
        return;
    }
    
    String userName = (String) session.getAttribute("userName");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - EduSummarize</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .admin-badge {
            background: linear-gradient(135deg, #ff4444 0%, #cc0000 100%);
            color: white;
            padding: 8px 16px;
            border-radius: 20px;
            font-size: 14px;
            display: inline-block;
            margin-left: 10px;
            font-weight: 600;
        }
        
        .admin-actions {
            background: white;
            border: 2px solid #000;
            padding: 40px;
            margin-bottom: 30px;
        }
        
        .action-btn {
            background: linear-gradient(135deg, #000 0%, #333 100%);
            color: white;
            padding: 20px;
            text-decoration: none;
            display: block;
            border: 2px solid #000;
            cursor: pointer;
            font-size: 16px;
            text-align: left;
            transition: all 0.3s ease;
            width: 100%;
        }
        
        .action-btn:hover {
            background: linear-gradient(135deg, #333 0%, #555 100%);
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
        }
        
        .action-btn small {
            display: block;
            margin-top: 5px;
            opacity: 0.8;
            font-size: 13px;
        }
        
        #displayArea {
            background: white;
            border: 2px solid #000;
            padding: 30px;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        
        table th, table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        
        table th {
            background: #000;
            color: white;
            font-weight: 600;
        }
        
        table tr:hover {
            background: #f5f5f5;
        }
    </style>
</head>
<body>
    
    <header class="header">
        <div class="header-container">
            <div class="logo">
                <h1>EduSummarize</h1>
                
            </div>
            <nav class="navbar">
                <a href="admin_dashboard.jsp" class="nav-link">Dashboard</a>
                <span class="nav-link">Welcome, <%= userName %>!</span>
                <a href="LogoutServlet" class="nav-link">Logout</a>
            </nav>
        </div>
    </header>

    <div class="container">
        <!-- Admin Operations -->
        <div class="admin-actions">
            <h2 style="margin-bottom: 30px; text-align: center;">Admin Operations</h2>
            
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px; margin-bottom: 20px;">
                <% if (adminLevel >= 1) { %>
                    <button class="action-btn" onclick="monitorSystemPerformance()">
                        <strong>Monitor System Performance</strong>
                        <br><small>View system statistics and metrics</small>
                    </button>
                <% } %>
                
                <% if (adminLevel >= 2) { %>
                    <button class="action-btn" onclick="manageUserAccess()">
                        <strong>Manage User Access</strong>
                        <br><small>Activate or deactivate user accounts</small>
                    </button>
                <% } %>
                
                <% if (adminLevel >= 1) { %>
                    <button class="action-btn" onclick="viewSubscription()">
                        <strong>View Subscriptions</strong>
                        <br><small>View all user subscription details</small>
                    </button>
                <% } %>
            </div>
            
            <p style="text-align: center; color: #666; font-size: 14px; margin-top: 20px;">
                Your Admin Level: <strong><%= adminLevel %></strong>
            </p>
        </div>

        <!-- Display Area -->
        <div class="admin-actions" id="displayArea" style="display: none;">
            <h3 id="displayTitle">Results</h3>
            <div id="displayContent"></div>
        </div>
    </div>

    <!-- Footer -->
    <div class="footer">
        <p>&copy; 2024 EduSummarize. All rights reserved. | Admin Panel</p>
    </div>

    <script>
        // CLASS DIAGRAM OPERATION 1: Admin.monitorSystemPerformance()
        function monitorSystemPerformance() {
            document.getElementById('displayTitle').textContent = 'System Performance Metrics';
            document.getElementById('displayContent').innerHTML = 'Loading...';
            document.getElementById('displayArea').style.display = 'block';
            
            fetch('AdminServlet?action=monitorSystemPerformance')
                .then(response => response.json())
                .then(data => {
                    // Display metrics in a clean table
                    let html = '<table>';
                    html += '<tr><th>Metric</th><th>Value</th></tr>';
                    html += '<tr><td>Total Users</td><td><strong>' + (data.totalUsers || '0') + '</strong></td></tr>';
                    html += '<tr><td>Premium Users</td><td><strong>' + (data.premiumUsers || '0') + '</strong></td></tr>';
                    html += '<tr><td>Free Users</td><td><strong>' + (data.freeUsers || '0') + '</strong></td></tr>';
                    html += '<tr><td>Monthly Revenue</td><td><strong>RM ' + (data.monthlyRevenue || '0') + '</strong></td></tr>';
                    html += '</table>';
                    
                    document.getElementById('displayContent').innerHTML = html;
                })
                .catch(error => {
                    document.getElementById('displayContent').innerHTML = '<p style="color: red;">Error loading statistics: ' + error.message + '</p>';
                });
        }

        // CLASS DIAGRAM OPERATION 2: Admin.manageUserAccess()
        function manageUserAccess() {
            document.getElementById('displayTitle').textContent = 'Manage User Access';
            document.getElementById('displayArea').style.display = 'block';
            
            // Get all users first
            fetch('AdminServlet?action=getAllUsers')
                .then(response => response.json())
                .then(users => {
                    let html = '<p>Select a user to activate/deactivate:</p>';
                    html += '<table>';
                    html += '<tr><th>ID</th>';
                    html += '<th>Name</th>';
                    html += '<th>Email</th>';
                    html += '<th>Subscription Status</th>';
                    html += '<th>Account Status</th>';
                    html += '<th>Actions</th></tr>';
                    
                    users.forEach(user => {
                        html += '<tr>';
                        html += '<td>' + user.userID + '</td>';
                        html += '<td>' + user.name + '</td>';
                        html += '<td>' + user.email + '</td>';
                        html += '<td>' + user.subscriptionStatus + '</td>';
                        html += '<td><strong style="color: ' + (user.accountStatus ? '#00cc00' : '#cc0000') + ';">' + (user.accountStatus ? 'Active' : 'Deactivated') + '</strong></td>';
                        html += '<td>';
                        html += '<button onclick="toggleUserAccess(' + user.userID + ', true)" style="background: #00cc00; color: white; padding: 5px 10px; border: none; margin-right: 5px; cursor: pointer;">Activate</button>';
                        html += '<button onclick="toggleUserAccess(' + user.userID + ', false)" style="background: #cc0000; color: white; padding: 5px 10px; border: none; cursor: pointer;">Deactivate</button>';
                        html += '</td></tr>';
                    });
                    html += '</table>';
                    
                    document.getElementById('displayContent').innerHTML = html;
                })
                .catch(error => {
                    document.getElementById('displayContent').innerHTML = 'Error loading users: ' + error;
                });
        }

        function toggleUserAccess(userId, activate) {
            if (!confirm('Are you sure you want to ' + (activate ? 'activate' : 'deactivate') + ' this user?')) {
                return;
            }
            
            fetch('AdminServlet?action=manageUserAccess&userId=' + userId + '&activate=' + activate, {
                method: 'POST'
            })
                .then(response => response.json())
                .then(data => {
                    alert(data.message);
                    if (data.success) {
                        manageUserAccess(); // Refresh the list
                    }
                })
                .catch(error => alert('Error: ' + error));
        }

        // CLASS DIAGRAM OPERATION 3: Admin.viewSubscription()
        function viewSubscription() {
            document.getElementById('displayTitle').textContent = 'View All Subscriptions';
            document.getElementById('displayContent').innerHTML = 'Loading...';
            document.getElementById('displayArea').style.display = 'block';
            
            const adminLevel = <%= adminLevel %>;
            
            fetch('AdminServlet?action=viewSubscription')
                .then(response => response.json())
                .then(subscriptions => {
                    let html = '<table>';
                    html += '<tr>';
                    html += '<th>User ID</th>';
                    html += '<th>Name</th>';
                    html += '<th>Email</th>';
                    html += '<th>Plan</th>';
                    html += '<th>PDF Count</th>';
                    html += '<th>Start Date</th>';
                    html += '<th>End Date</th>';
                    if (adminLevel >= 2) {
                        html += '<th>Actions</th>';
                    }
                    html += '</tr>';
                    
                    subscriptions.forEach(sub => {
                        html += '<tr>';
                        html += '<td>' + sub.userID + '</td>';
                        html += '<td>' + sub.userName + '</td>';
                        html += '<td>' + sub.email + '</td>';
                        html += '<td><strong style="color: ' + (sub.planType === 'Premium' ? '#FFD700' : '#666') + ';">' + sub.planType + '</strong></td>';
                        html += '<td>' + sub.pdfCount + '</td>';
                        html += '<td>' + (sub.startDate || 'N/A') + '</td>';
                        html += '<td>' + (sub.endDate || 'N/A') + '</td>';
                        if (adminLevel >= 2) {
                            html += '<td>';
                            html += '<button onclick="toggleUserSubscription(' + sub.userID + ', true)" style="background: #FFD700; color: #000; padding: 5px 10px; border: none; margin-right: 5px; cursor: pointer; font-weight: 600;">Activate Premium</button>';
                            html += '<button onclick="toggleUserSubscription(' + sub.userID + ', false)" style="background: #666; color: white; padding: 5px 10px; border: none; cursor: pointer;">Set to Free</button>';
                            html += '</td>';
                        }
                        html += '</tr>';
                    });
                    html += '</table>';
                    html += '<p style="margin-top: 20px;"><strong>Total Subscriptions:</strong> ' + subscriptions.length + '</p>';
                    
                    document.getElementById('displayContent').innerHTML = html;
                })
                .catch(error => {
                    document.getElementById('displayContent').innerHTML = 'Error loading subscriptions: ' + error;
                });
        }

        function toggleUserSubscription(userId, activate) {
            const action = activate ? 'activate Premium subscription' : 'set to Free plan';
            if (!confirm('Are you sure you want to ' + action + ' for this user?')) {
                return;
            }
            
            fetch('AdminServlet?action=manageUserSubscription&userId=' + userId + '&activate=' + activate, {
                method: 'POST'
            })
                .then(response => response.json())
                .then(data => {
                    alert(data.message);
                    if (data.success) {
                        viewSubscription(); // Refresh the list
                    }
                })
                .catch(error => alert('Error: ' + error));
        }

        // Load stats on page load
        window.onload = function() {
            monitorSystemPerformance();
        };
    </script>
</body>
</html>
