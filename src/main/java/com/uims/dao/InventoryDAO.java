package com.uims.dao;

import com.uims.db.DBConnection;
import com.uims.model.InventoryItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * InventoryDAO – Data Access Object for the <code>Inventory</code> table.
 *
 * Provides CRUD operations:
 *   getAllItems()    – list all inventory items
 *   getItemById()   – find one by primary key
 *   addItem()       – insert a new item
 *   updateItem()    – update an existing item
 *   deleteItem()    – remove an item
 */
public class InventoryDAO {

    /** Returns every inventory item, ordered by name. */
    public List<InventoryItem> getAllItems() {
        List<InventoryItem> list = new ArrayList<>();
        String sql = "SELECT item_id, item_name, category, quantity, unit, description, "
                   + "date_added, added_by FROM Inventory ORDER BY item_name";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Fetches a single inventory item by primary key. */
    public InventoryItem getItemById(int itemId) {
        String sql = "SELECT item_id, item_name, category, quantity, unit, description, "
                   + "date_added, added_by FROM Inventory WHERE item_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts a new inventory item.
     *
     * @param item     the item to insert
     * @param addedBy  the user_id of the logged-in user performing the insert
     * @return {@code true} if the insert succeeded
     */
    public boolean addItem(InventoryItem item, int addedBy) {
        String sql = "INSERT INTO Inventory (item_name, category, quantity, unit, description, added_by) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getItemName());
            ps.setString(2, item.getCategory());
            ps.setInt(3, item.getQuantity());
            ps.setString(4, item.getUnit());
            ps.setString(5, item.getDescription());
            ps.setInt(6, addedBy);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates an existing inventory item.
     *
     * @param item the item containing updated values; must have a valid itemId
     * @return {@code true} if a row was updated
     */
    public boolean updateItem(InventoryItem item) {
        String sql = "UPDATE Inventory SET item_name = ?, category = ?, quantity = ?, "
                   + "unit = ?, description = ? WHERE item_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getItemName());
            ps.setString(2, item.getCategory());
            ps.setInt(3, item.getQuantity());
            ps.setString(4, item.getUnit());
            ps.setString(5, item.getDescription());
            ps.setInt(6, item.getItemId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes an inventory item by primary key.
     *
     * @param itemId the ID of the item to delete
     * @return {@code true} if a row was deleted
     */
    public boolean deleteItem(int itemId) {
        String sql = "DELETE FROM Inventory WHERE item_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, itemId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Maps the current {@link ResultSet} row to an {@link InventoryItem} object. */
    private InventoryItem mapRow(ResultSet rs) throws SQLException {
        InventoryItem item = new InventoryItem();
        item.setItemId(rs.getInt("item_id"));
        item.setItemName(rs.getString("item_name"));
        item.setCategory(rs.getString("category"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnit(rs.getString("unit"));
        item.setDescription(rs.getString("description"));
        item.setDateAdded(rs.getTimestamp("date_added"));
        item.setAddedBy(rs.getInt("added_by"));
        return item;
    }
}
