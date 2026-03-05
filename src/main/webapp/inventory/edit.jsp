<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.uims.model.User" %>
<%@ page import="com.uims.model.InventoryItem" %>
<%
    /* Access control */
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    InventoryItem item = (InventoryItem) request.getAttribute("item");
    if (item == null) {
        response.sendRedirect(request.getContextPath() + "/inventory?action=list");
        return;
    }
    String errorMessage = (String) request.getAttribute("errorMessage");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Inventory Item – UIMS</title>
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
        <h2 style="color:#1a3c5e; font-size:20px;">&#9998; Edit Inventory Item</h2>
        <a href="<%= request.getContextPath() %>/inventory?action=list" class="btn btn-secondary">
            &larr; Back to List
        </a>
    </div>

    <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
        <div class="alert alert-error"><%= errorMessage %></div>
    <% } %>

    <div class="card">
        <form action="<%= request.getContextPath() %>/inventory" method="post">
            <input type="hidden" name="action"  value="edit">
            <input type="hidden" name="itemId"  value="<%= item.getItemId() %>">

            <div class="form-group">
                <label for="itemName">Item Name <span style="color:red">*</span></label>
                <input type="text" id="itemName" name="itemName"
                       value="<%= item.getItemName() %>" required maxlength="150">
            </div>

            <div class="form-group">
                <label for="category">Category <span style="color:red">*</span></label>
                <input type="text" id="category" name="category"
                       value="<%= item.getCategory() %>" required maxlength="100">
            </div>

            <div class="form-group">
                <label for="quantity">Quantity <span style="color:red">*</span></label>
                <input type="number" id="quantity" name="quantity"
                       value="<%= item.getQuantity() %>" min="0" required>
            </div>

            <div class="form-group">
                <label for="unit">Unit <span style="color:red">*</span></label>
                <select id="unit" name="unit" required>
                    <option value="piece"  <%= "piece".equals(item.getUnit())  ? "selected" : "" %>>Piece</option>
                    <option value="box"    <%= "box".equals(item.getUnit())    ? "selected" : "" %>>Box</option>
                    <option value="set"    <%= "set".equals(item.getUnit())    ? "selected" : "" %>>Set</option>
                    <option value="kg"     <%= "kg".equals(item.getUnit())     ? "selected" : "" %>>Kilogram (kg)</option>
                    <option value="litre"  <%= "litre".equals(item.getUnit())  ? "selected" : "" %>>Litre</option>
                    <option value="ream"   <%= "ream".equals(item.getUnit())   ? "selected" : "" %>>Ream</option>
                    <option value="carton" <%= "carton".equals(item.getUnit()) ? "selected" : "" %>>Carton</option>
                    <option value="other"  <%= "other".equals(item.getUnit())  ? "selected" : "" %>>Other</option>
                </select>
            </div>

            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description"><%= item.getDescription() != null ? item.getDescription() : "" %></textarea>
            </div>

            <div class="form-group mt-10">
                <button type="submit" class="btn btn-primary">Update Item</button>
                <a href="<%= request.getContextPath() %>/inventory?action=list"
                   class="btn btn-secondary" style="margin-left:10px;">Cancel</a>
            </div>
        </form>
    </div>

</div><!-- /container -->

</body>
</html>
