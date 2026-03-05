package com.uims.controller;

import com.uims.dao.AllocationDAO;
import com.uims.dao.DepartmentDAO;
import com.uims.dao.InventoryDAO;
import com.uims.model.Allocation;
import com.uims.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * AllocationServlet – manages all department-allocation requests.
 *
 * URL pattern: /allocation
 *
 * Supported actions (passed as the "action" query/form parameter):
 *   list   (GET)  – display all issued items
 *   issue  (GET)  – show the issue-item form
 *   issue  (POST) – process the issue form
 *   delete (GET)  – delete an allocation record (Admin only)
 *
 * Access control: all actions require a valid session.
 */
@WebServlet("/allocation")
public class AllocationServlet extends HttpServlet {

    private final AllocationDAO  allocationDAO  = new AllocationDAO();
    private final InventoryDAO   inventoryDAO   = new InventoryDAO();
    private final DepartmentDAO  departmentDAO  = new DepartmentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request, response)) return;

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "issue":
                // Populate dropdown data for the form
                request.setAttribute("items",       inventoryDAO.getAllItems());
                request.setAttribute("departments", departmentDAO.getAllDepartments());
                request.getRequestDispatcher("/allocation/issue.jsp").forward(request, response);
                break;

            case "delete":
                if (!isAdmin(request)) {
                    response.sendRedirect(request.getContextPath() + "/allocation?action=list&error=notauthorized");
                    return;
                }
                int deleteId = parseId(request.getParameter("allocationId"));
                if (deleteId > 0) {
                    allocationDAO.deleteAllocation(deleteId);
                }
                response.sendRedirect(request.getContextPath() + "/allocation?action=list");
                break;

            default:
                doList(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request, response)) return;

        String action = request.getParameter("action");
        if ("issue".equals(action)) {
            handleIssue(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/allocation?action=list");
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private void doList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Allocation> allocations = allocationDAO.getAllAllocations();
        request.setAttribute("allocations", allocations);
        request.getRequestDispatcher("/allocation/list.jsp").forward(request, response);
    }

    private void handleIssue(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String itemIdStr    = request.getParameter("itemId");
        String deptIdStr    = request.getParameter("departmentId");
        String qtyStr       = request.getParameter("quantityIssued");
        String remarks      = request.getParameter("remarks");

        if (isEmpty(itemIdStr) || isEmpty(deptIdStr) || isEmpty(qtyStr)) {
            request.setAttribute("errorMessage", "All required fields must be filled in.");
            request.setAttribute("items",       inventoryDAO.getAllItems());
            request.setAttribute("departments", departmentDAO.getAllDepartments());
            request.getRequestDispatcher("/allocation/issue.jsp").forward(request, response);
            return;
        }

        try {
            int itemId       = Integer.parseInt(itemIdStr.trim());
            int departmentId = Integer.parseInt(deptIdStr.trim());
            int quantity     = Integer.parseInt(qtyStr.trim());

            if (quantity <= 0) {
                request.setAttribute("errorMessage", "Quantity must be greater than zero.");
                request.setAttribute("items",       inventoryDAO.getAllItems());
                request.setAttribute("departments", departmentDAO.getAllDepartments());
                request.getRequestDispatcher("/allocation/issue.jsp").forward(request, response);
                return;
            }

            Allocation allocation = new Allocation();
            allocation.setItemId(itemId);
            allocation.setDepartmentId(departmentId);
            allocation.setQuantityIssued(quantity);
            allocation.setIssuedBy(getLoggedInUser(request).getUserId());
            allocation.setRemarks(remarks == null ? "" : remarks.trim());

            boolean success = allocationDAO.addAllocation(allocation);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/allocation?action=list&success=issued");
            } else {
                request.setAttribute("errorMessage",
                    "Failed to issue item. There may not be enough stock available.");
                request.setAttribute("items",       inventoryDAO.getAllItems());
                request.setAttribute("departments", departmentDAO.getAllDepartments());
                request.getRequestDispatcher("/allocation/issue.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid numeric value provided.");
            request.setAttribute("items",       inventoryDAO.getAllItems());
            request.setAttribute("departments", departmentDAO.getAllDepartments());
            request.getRequestDispatcher("/allocation/issue.jsp").forward(request, response);
        }
    }

    private boolean isLoggedIn(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    private boolean isAdmin(HttpServletRequest request) {
        User user = getLoggedInUser(request);
        return user != null && user.isAdmin();
    }

    private User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (User) session.getAttribute("loggedInUser");
    }

    private int parseId(String value) {
        if (value == null || value.trim().isEmpty()) return -1;
        try { return Integer.parseInt(value.trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
