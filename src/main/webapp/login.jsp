<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login – University Inventory Management System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<div class="login-wrapper">
    <div class="login-box">
        <h1>&#127979; UIMS</h1>
        <p class="subtitle">University Inventory Management System</p>

        <%-- Display error message if login failed --%>
        <% String error = (String) request.getAttribute("errorMessage"); %>
        <% if (error != null && !error.isEmpty()) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>

        <form action="<%= request.getContextPath() %>/login" method="post">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username"
                       placeholder="Enter your username" required autofocus>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password"
                       placeholder="Enter your password" required>
            </div>
            <div class="form-group mt-10">
                <button type="submit" class="btn btn-primary" style="width:100%;">
                    Login
                </button>
            </div>
        </form>

        <p class="text-center mt-10" style="font-size:12px; color:#999;">
            Default accounts &mdash; Admin: <b>admin / admin123</b> &nbsp;|&nbsp; Storekeeper: <b>storekeeper / store123</b>
        </p>
    </div>
</div>

</body>
</html>
