<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.uims.model.User" %>
<%
    /* Access control: redirect to login if no session */
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard – UIMS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<%-- Navigation Bar --%>
<div class="navbar">
    <span class="brand">&#127979; University Inventory Management System</span>
    <nav>
        <a href="<%= request.getContextPath() %>/dashboard">Dashboard</a>
        <a href="<%= request.getContextPath() %>/inventory?action=list">Inventory</a>
        <a href="<%= request.getContextPath() %>/allocation?action=list">Allocations</a>
        <% if (loggedInUser.isAdmin()) { %>
            <a href="<%= request.getContextPath() %>/inventory?action=add">Add Item</a>
        <% } %>
        <a href="<%= request.getContextPath() %>/logout">Logout</a>
    </nav>
    <span class="user-info">
        &#128100; <%= loggedInUser.getFullName() %>
        <span class="badge <%= loggedInUser.isAdmin() ? "badge-admin" : "badge-storekeeper" %>">
            <%= loggedInUser.getRole() %>
        </span>
    </span>
</div>

<div class="container">

    <div class="card">
        <h2>Welcome, <%= loggedInUser.getFullName() %>!</h2>
        <p style="color:#555;">
            You are logged in as <strong><%= loggedInUser.getRole() %></strong>.
            Use the navigation links above or the quick-action cards below to get started.
        </p>
    </div>

    <%-- Quick Action Cards --%>
    <div class="quick-links mb-20">
        <a href="<%= request.getContextPath() %>/inventory?action=list" class="quick-link-card">
            <div class="icon">&#128230;</div>
            <div class="label">View Inventory</div>
        </a>
        <a href="<%= request.getContextPath() %>/inventory?action=add" class="quick-link-card">
            <div class="icon">&#10133;</div>
            <div class="label">Add Inventory Item</div>
        </a>
        <a href="<%= request.getContextPath() %>/allocation?action=issue" class="quick-link-card">
            <div class="icon">&#128196;</div>
            <div class="label">Issue Item to Department</div>
        </a>
        <a href="<%= request.getContextPath() %>/allocation?action=list" class="quick-link-card">
            <div class="icon">&#128203;</div>
            <div class="label">View Issued Items</div>
        </a>
    </div>

    <%--
    ================================================================
    TODO – Future Modules (placeholders for extended development)
    ================================================================
    1. Reports Module
       - Generate PDF/Excel inventory reports
       - Usage statistics by department
       - Low-stock alerts report

    2. Department Management Module
       - Add / edit / remove departments
       - View department-wise allocation summary

    3. User Management Module (Admin only)
       - Add / edit / deactivate system users
       - Password reset functionality

    4. Advanced Features
       - Barcode / QR code scanning for items
       - Email notifications on low stock
       - Audit trail / activity log
       - Role-based dashboard analytics
    ================================================================
    --%>

    <div class="card">
        <h2>System Information</h2>
        <table>
            <tr>
                <td style="font-weight:600; width:200px;">Application</td>
                <td>University Inventory Management System (UIMS)</td>
            </tr>
            <tr>
                <td style="font-weight:600;">Version</td>
                <td>1.0</td>
            </tr>
            <tr>
                <td style="font-weight:600;">Technology</td>
                <td>Java Servlets &amp; JSP, MySQL, Apache Tomcat</td>
            </tr>
            <tr>
                <td style="font-weight:600;">Your Username</td>
                <td><%= loggedInUser.getUsername() %></td>
            </tr>
            <tr>
                <td style="font-weight:600;">Your Role</td>
                <td><%= loggedInUser.getRole() %></td>
            </tr>
        </table>
    </div>

</div><!-- /container -->

</body>
</html>
