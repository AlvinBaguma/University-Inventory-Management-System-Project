package com.uims.controller;

import com.uims.dao.InventoryDAO;
import com.uims.model.InventoryItem;
import com.uims.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * InventoryServlet – manages all inventory-related requests.
 *
 * URL pattern: /inventory
 *
 * Supported actions (passed as the "action" query/form parameter):
 *   list   (GET)  – display all inventory items
 *   add    (GET)  – show the add-item form
 *   add    (POST) – process the add-item form
 *   edit   (GET)  – show the edit-item form for a given itemId
 *   edit   (POST) – process the edit-item form
 *   delete (GET)  – delete the item with the given itemId and redirect to list
 *
 * Access control: all actions require a valid session.
 * Administrator and Storekeeper can view and add items.
 * Only Administrators can delete items.
 */
@WebServlet("/inventory")
public class InventoryServlet extends HttpServlet {

    private final InventoryDAO inventoryDAO = new InventoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request, response)) return;

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "add":
                request.getRequestDispatcher("/inventory/add.jsp").forward(request, response);
                break;

            case "edit":
                int editId = parseId(request.getParameter("itemId"));
                if (editId <= 0) {
                    response.sendRedirect(request.getContextPath() + "/inventory?action=list");
                    return;
                }
                InventoryItem itemToEdit = inventoryDAO.getItemById(editId);
                if (itemToEdit == null) {
                    request.setAttribute("errorMessage", "Item not found.");
                    doList(request, response);
                    return;
                }
                request.setAttribute("item", itemToEdit);
                request.getRequestDispatcher("/inventory/edit.jsp").forward(request, response);
                break;

            case "delete":
                if (!isAdmin(request)) {
                    response.sendRedirect(request.getContextPath() + "/inventory?action=list&error=notauthorized");
                    return;
                }
                int deleteId = parseId(request.getParameter("itemId"));
                if (deleteId > 0) {
                    inventoryDAO.deleteItem(deleteId);
                }
                response.sendRedirect(request.getContextPath() + "/inventory?action=list");
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
        if (action == null) action = "list";

        switch (action) {
            case "add":
                handleAdd(request, response);
                break;
            case "edit":
                handleEdit(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/inventory?action=list");
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /** Loads all items and forwards to the list JSP. */
    private void doList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<InventoryItem> items = inventoryDAO.getAllItems();
        request.setAttribute("items", items);
        request.getRequestDispatcher("/inventory/list.jsp").forward(request, response);
    }

    /** Processes the "add item" form. */
    private void handleAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        InventoryItem item = buildItemFromRequest(request);
        if (item == null) {
            request.setAttribute("errorMessage", "All required fields must be filled in.");
            request.getRequestDispatcher("/inventory/add.jsp").forward(request, response);
            return;
        }

        User loggedInUser = getLoggedInUser(request);
        boolean success = inventoryDAO.addItem(item, loggedInUser.getUserId());
        if (success) {
            response.sendRedirect(request.getContextPath() + "/inventory?action=list&success=added");
        } else {
            request.setAttribute("errorMessage", "Failed to add item. Please try again.");
            request.getRequestDispatcher("/inventory/add.jsp").forward(request, response);
        }
    }

    /** Processes the "edit item" form. */
    private void handleEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int itemId = parseId(request.getParameter("itemId"));
        if (itemId <= 0) {
            response.sendRedirect(request.getContextPath() + "/inventory?action=list");
            return;
        }

        InventoryItem item = buildItemFromRequest(request);
        if (item == null) {
            request.setAttribute("errorMessage", "All required fields must be filled in.");
            InventoryItem existing = inventoryDAO.getItemById(itemId);
            request.setAttribute("item", existing);
            request.getRequestDispatcher("/inventory/edit.jsp").forward(request, response);
            return;
        }
        item.setItemId(itemId);

        boolean success = inventoryDAO.updateItem(item);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/inventory?action=list&success=updated");
        } else {
            request.setAttribute("errorMessage", "Failed to update item. Please try again.");
            request.setAttribute("item", item);
            request.getRequestDispatcher("/inventory/edit.jsp").forward(request, response);
        }
    }

    /**
     * Builds an {@link InventoryItem} from request parameters.
     * Returns {@code null} if any required field is missing or invalid.
     */
    private InventoryItem buildItemFromRequest(HttpServletRequest request) {
        String itemName    = request.getParameter("itemName");
        String category    = request.getParameter("category");
        String quantityStr = request.getParameter("quantity");
        String unit        = request.getParameter("unit");
        String description = request.getParameter("description");

        if (isEmpty(itemName) || isEmpty(category) || isEmpty(quantityStr) || isEmpty(unit)) {
            return null;
        }
        try {
            int quantity = Integer.parseInt(quantityStr.trim());
            if (quantity < 0) return null;
            InventoryItem item = new InventoryItem();
            item.setItemName(itemName.trim());
            item.setCategory(category.trim());
            item.setQuantity(quantity);
            item.setUnit(unit.trim());
            item.setDescription(description == null ? "" : description.trim());
            return item;
        } catch (NumberFormatException e) {
            return null;
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
