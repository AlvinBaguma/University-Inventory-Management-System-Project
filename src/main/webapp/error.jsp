<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    String  message    = (String)  request.getAttribute("javax.servlet.error.message");
    if (statusCode == null) statusCode = 0;
    if (message    == null || message.isEmpty()) message = "An unexpected error occurred.";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error – UIMS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="login-wrapper">
    <div class="login-box" style="text-align:center;">
        <h1 style="font-size:48px; color:#dc3545;"><%= statusCode > 0 ? statusCode : "&#9888;" %></h1>
        <p style="color:#555; margin:16px 0;"><%= message %></p>
        <a href="<%= request.getContextPath() %>/dashboard" class="btn btn-primary">Go to Dashboard</a>
    </div>
</div>
</body>
</html>
