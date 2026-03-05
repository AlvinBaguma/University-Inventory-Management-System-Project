package com.uims.controller;

import com.uims.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * DashboardServlet – serves the post-login landing page.
 *
 * GET /dashboard → verifies the session and forwards to dashboard.jsp
 *
 * Redirects unauthenticated requests back to the login page.
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("loggedInUser");
        request.setAttribute("user", user);
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}
