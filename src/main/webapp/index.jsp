<%-- index.jsp – redirect to login --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%
    response.sendRedirect(request.getContextPath() + "/login");
%>
