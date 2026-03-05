package com.uims.dao;

import com.uims.db.DBConnection;
import com.uims.model.Allocation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AllocationDAO – Data Access Object for the <code>Allocation</code> table.
 *
 * Provides operations:
 *   getAllAllocations() – list all issued items with item/department names
 *   addAllocation()     – record a new issue transaction
 *   deleteAllocation()  – remove an allocation record
 *
 * The list query uses a JOIN so that the returned objects carry the
 * human-readable item name, department name, and issuer name rather
 * than just foreign-key integers.
 */
public class AllocationDAO {

    /**
     * Returns all allocation records joined with item, department, and user names.
     * Results are ordered newest first.
     */
    public List<Allocation> getAllAllocations() {
        List<Allocation> list = new ArrayList<>();
        String sql =
            "SELECT a.allocation_id, a.item_id, a.department_id, a.quantity_issued, "
          + "       a.issued_by, a.issue_date, a.remarks, "
          + "       i.item_name, d.dept_name, u.full_name AS issued_by_name "
          + "FROM Allocation a "
          + "JOIN Inventory   i ON a.item_id       = i.item_id "
          + "JOIN Departments d ON a.department_id = d.department_id "
          + "LEFT JOIN Users  u ON a.issued_by     = u.user_id "
          + "ORDER BY a.issue_date DESC";

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

    /**
     * Inserts a new allocation record and decrements the item's available quantity.
     * Both operations run inside a single transaction so that a partial failure
     * leaves the database consistent.
     *
     * @param allocation the allocation to persist
     * @return {@code true} if the transaction committed successfully
     */
    public boolean addAllocation(Allocation allocation) {
        String insertSql = "INSERT INTO Allocation "
                         + "(item_id, department_id, quantity_issued, issued_by, remarks) "
                         + "VALUES (?, ?, ?, ?, ?)";
        String updateSql = "UPDATE Inventory SET quantity = quantity - ? WHERE item_id = ? AND quantity >= ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // start transaction

            // 1. Decrement inventory quantity
            try (PreparedStatement upStmt = conn.prepareStatement(updateSql)) {
                upStmt.setInt(1, allocation.getQuantityIssued());
                upStmt.setInt(2, allocation.getItemId());
                upStmt.setInt(3, allocation.getQuantityIssued());
                int rowsUpdated = upStmt.executeUpdate();
                if (rowsUpdated == 0) {
                    // Not enough stock – roll back
                    conn.rollback();
                    return false;
                }
            }

            // 2. Insert allocation record
            try (PreparedStatement insStmt = conn.prepareStatement(insertSql)) {
                insStmt.setInt(1, allocation.getItemId());
                insStmt.setInt(2, allocation.getDepartmentId());
                insStmt.setInt(3, allocation.getQuantityIssued());
                insStmt.setInt(4, allocation.getIssuedBy());
                insStmt.setString(5, allocation.getRemarks());
                insStmt.executeUpdate();
            }

            conn.commit(); // commit both operations together
            conn.setAutoCommit(true); // restore default before returning/closing
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // ensure auto-commit is restored before close
                    conn.close();
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
        return false;
    }

    /**
     * Deletes an allocation record by primary key.
     *
     * @param allocationId the ID to delete
     * @return {@code true} if a row was deleted
     */
    public boolean deleteAllocation(int allocationId) {
        String sql = "DELETE FROM Allocation WHERE allocation_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, allocationId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Maps the current {@link ResultSet} row to an {@link Allocation} object. */
    private Allocation mapRow(ResultSet rs) throws SQLException {
        Allocation a = new Allocation();
        a.setAllocationId(rs.getInt("allocation_id"));
        a.setItemId(rs.getInt("item_id"));
        a.setDepartmentId(rs.getInt("department_id"));
        a.setQuantityIssued(rs.getInt("quantity_issued"));
        a.setIssuedBy(rs.getInt("issued_by"));
        a.setIssueDate(rs.getTimestamp("issue_date"));
        a.setRemarks(rs.getString("remarks"));
        a.setItemName(rs.getString("item_name"));
        a.setDeptName(rs.getString("dept_name"));
        a.setIssuedByName(rs.getString("issued_by_name"));
        return a;
    }
}
