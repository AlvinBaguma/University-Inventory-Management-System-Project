<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.uims.model.User" %>
<%@ page import="com.uims.model.InventoryItem" %>
<%@ page import="com.uims.model.Department" %>
<%@ page import="java.util.List" %>
<%
    /* Access control */
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    List<InventoryItem> items       = (List<InventoryItem>) request.getAttribute("items");
    List<Department>    departments = (List<Department>)    request.getAttribute("departments");
    String errorMessage = (String) request.getAttribute("errorMessage");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Issue Item to Department – UIMS</title>
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
        <h2 style="color:#1a3c5e; font-size:20px;">&#128196; Issue Item to Department</h2>
        <a href="<%= request.getContextPath() %>/allocation?action=list" class="btn btn-secondary">
            &larr; Back to Allocations
        </a>
    </div>

    <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
        <div class="alert alert-error"><%= errorMessage %></div>
    <% } %>

    <div class="card">
        <form action="<%= request.getContextPath() %>/allocation" method="post">
            <input type="hidden" name="action" value="issue">

            <div class="form-group">
                <label for="itemId">Select Item <span style="color:red">*</span></label>
                <select id="itemId" name="itemId" required>
                    <option value="">-- Select an Inventory Item --</option>
                    <% if (items != null) {
                           for (InventoryItem item : items) {
                               if (item.getQuantity() > 0) { %>
                        <option value="<%= item.getItemId() %>">
                            <%= item.getItemName() %> (Available: <%= item.getQuantity() %> <%= item.getUnit() %>)
                        </option>
                    <%     }
                           }
                       } %>
                </select>
            </div>

            <div class="form-group">
                <label for="departmentId">Select Department <span style="color:red">*</span></label>
                <select id="departmentId" name="departmentId" required>
                    <option value="">-- Select a Department --</option>
                    <% if (departments != null) {
                           for (Department dept : departments) { %>
                        <option value="<%= dept.getDepartmentId() %>">
                            <%= dept.getDeptName() %> (<%= dept.getDeptCode() %>)
                        </option>
                    <%     }
                       } %>
                </select>
            </div>

            <div class="form-group">
                <label for="quantityIssued">Quantity to Issue <span style="color:red">*</span></label>
                <input type="number" id="quantityIssued" name="quantityIssued"
                       placeholder="1" min="1" required>
            </div>

            <div class="form-group">
                <label for="remarks">Remarks / Notes</label>
                <textarea id="remarks" name="remarks"
                          placeholder="Optional: purpose of issue, requisition number, etc."></textarea>
            </div>

            <div class="form-group mt-10">
                <button type="submit" class="btn btn-primary">Issue Item</button>
                <a href="<%= request.getContextPath() %>/allocation?action=list"
                   class="btn btn-secondary" style="margin-left:10px;">Cancel</a>
            </div>
        </form>
    </div>

</div><!-- /container -->

</body>
</html>
