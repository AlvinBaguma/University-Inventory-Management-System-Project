<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.uims.model.User" %>
<%@ page import="com.uims.model.Allocation" %>
<%@ page import="java.util.List" %>
<%
    /* Access control */
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    List<Allocation> allocations = (List<Allocation>) request.getAttribute("allocations");
    String successMsg = request.getParameter("success");
    String errorParam = request.getParameter("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Allocations – UIMS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<%-- Navigation Bar --%>
<div class="navbar">
    <span class="brand">&#127979; UIMS</span>
    <nav>
        <a href="<%= request.getContextPath() %>/dashboard">Dashboard</a>
        <a href="<%= request.getContextPath() %>/inventory?action=list">Inventory</a>
        <a href="<%= request.getContextPath() %>/allocation?action=list">Allocations</a>
        <a href="<%= request.getContextPath() %>/logout">Logout</a>
    </nav>
    <span class="user-info">&#128100; <%= loggedInUser.getFullName() %></span>
</div>

<div class="container">

    <div class="page-header">
        <h2 style="color:#1a3c5e; font-size:20px;">&#128203; Issued Items (Allocations)</h2>
        <a href="<%= request.getContextPath() %>/allocation?action=issue" class="btn btn-success">
            + Issue Item to Department
        </a>
    </div>

    <%-- Feedback messages --%>
    <% if ("issued".equals(successMsg)) { %>
        <div class="alert alert-success">Item issued to department successfully!</div>
    <% } else if ("notauthorized".equals(errorParam)) { %>
        <div class="alert alert-error">You are not authorized to perform that action.</div>
    <% } %>

    <div class="card">
        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Item</th>
                        <th>Department</th>
                        <th>Qty Issued</th>
                        <th>Issued By</th>
                        <th>Issue Date</th>
                        <th>Remarks</th>
                        <% if (loggedInUser.isAdmin()) { %><th>Actions</th><% } %>
                    </tr>
                </thead>
                <tbody>
                    <% if (allocations == null || allocations.isEmpty()) { %>
                        <tr>
                            <td colspan="<%= loggedInUser.isAdmin() ? 8 : 7 %>"
                                class="text-center" style="padding:30px; color:#888;">
                                No allocations recorded yet.
                                <a href="<%= request.getContextPath() %>/allocation?action=issue">
                                    Issue the first item</a>.
                            </td>
                        </tr>
                    <% } else {
                         int rowNum = 1;
                         for (Allocation a : allocations) { %>
                        <tr>
                            <td><%= rowNum++ %></td>
                            <td><strong><%= a.getItemName() %></strong></td>
                            <td><%= a.getDeptName() %></td>
                            <td><%= a.getQuantityIssued() %></td>
                            <td><%= a.getIssuedByName() != null ? a.getIssuedByName() : "N/A" %></td>
                            <td style="white-space:nowrap;">
                                <%= a.getIssueDate() != null ? a.getIssueDate().toString().substring(0, 16) : "" %>
                            </td>
                            <td><%= a.getRemarks() != null ? a.getRemarks() : "" %></td>
                            <% if (loggedInUser.isAdmin()) { %>
                                <td>
                                    <a href="<%= request.getContextPath() %>/allocation?action=delete&allocationId=<%= a.getAllocationId() %>"
                                       class="btn btn-danger btn-sm"
                                       onclick="return confirm('Delete this allocation record?');">
                                        Delete
                                    </a>
                                </td>
                            <% } %>
                        </tr>
                    <% } } %>
                </tbody>
            </table>
        </div>
    </div>

</div><!-- /container -->

</body>
</html>
