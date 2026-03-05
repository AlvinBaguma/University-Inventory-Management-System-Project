<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.uims.model.User" %>
<%@ page import="com.uims.model.InventoryItem" %>
<%@ page import="java.util.List" %>
<%
    /* Access control */
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    List<InventoryItem> items = (List<InventoryItem>) request.getAttribute("items");
    String successMsg = request.getParameter("success");
    String errorParam = request.getParameter("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventory – UIMS</title>
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
        <h2 style="color:#1a3c5e; font-size:20px;">&#128230; Inventory Items</h2>
        <a href="<%= request.getContextPath() %>/inventory?action=add" class="btn btn-success">
            + Add New Item
        </a>
    </div>

    <%-- Success/error feedback --%>
    <% if ("added".equals(successMsg)) { %>
        <div class="alert alert-success">Item added successfully!</div>
    <% } else if ("updated".equals(successMsg)) { %>
        <div class="alert alert-success">Item updated successfully!</div>
    <% } else if ("notauthorized".equals(errorParam)) { %>
        <div class="alert alert-error">You are not authorized to perform that action.</div>
    <% } %>

    <div class="card">
        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Item Name</th>
                        <th>Category</th>
                        <th>Quantity</th>
                        <th>Unit</th>
                        <th>Description</th>
                        <th>Date Added</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (items == null || items.isEmpty()) { %>
                        <tr>
                            <td colspan="8" class="text-center" style="padding:30px; color:#888;">
                                No inventory items found.
                                <a href="<%= request.getContextPath() %>/inventory?action=add">Add the first item</a>.
                            </td>
                        </tr>
                    <% } else {
                         int rowNum = 1;
                         for (InventoryItem item : items) { %>
                        <tr>
                            <td><%= rowNum++ %></td>
                            <td><strong><%= item.getItemName() %></strong></td>
                            <td><%= item.getCategory() %></td>
                            <td>
                                <% if (item.getQuantity() == 0) { %>
                                    <span style="color:#dc3545; font-weight:600;">0</span>
                                <% } else if (item.getQuantity() < 5) { %>
                                    <span style="color:#ffc107; font-weight:600;"><%= item.getQuantity() %></span>
                                <% } else { %>
                                    <span style="color:#198754; font-weight:600;"><%= item.getQuantity() %></span>
                                <% } %>
                            </td>
                            <td><%= item.getUnit() %></td>
                            <td><%= item.getDescription() != null ? item.getDescription() : "" %></td>
                            <td style="white-space:nowrap;">
                                <%= item.getDateAdded() != null ? item.getDateAdded().toString().substring(0, 10) : "" %>
                            </td>
                            <td style="white-space:nowrap;">
                                <a href="<%= request.getContextPath() %>/inventory?action=edit&itemId=<%= item.getItemId() %>"
                                   class="btn btn-warning btn-sm">Edit</a>
                                <% if (loggedInUser.isAdmin()) { %>
                                    <a href="<%= request.getContextPath() %>/inventory?action=delete&itemId=<%= item.getItemId() %>"
                                       class="btn btn-danger btn-sm"
                                       onclick="return confirm('Delete this item? This action cannot be undone.');">
                                        Delete
                                    </a>
                                <% } %>
                            </td>
                        </tr>
                    <% } } %>
                </tbody>
            </table>
        </div>
    </div>

</div><!-- /container -->

</body>
</html>
