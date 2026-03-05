package com.uims.dao;

import com.uims.db.DBConnection;
import com.uims.model.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DepartmentDAO – Data Access Object for the <code>Departments</code> table.
 *
 * Provides basic CRUD operations:
 *   getAllDepartments() – list all departments
 *   getDepartmentById() – find one by primary key
 *   addDepartment()     – insert a new department
 *   updateDepartment()  – update an existing department
 *   deleteDepartment()  – remove a department
 */
public class DepartmentDAO {

    /** Returns every department in the database, ordered by name. */
    public List<Department> getAllDepartments() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT department_id, dept_name, dept_code, contact_person, created_at "
                   + "FROM Departments ORDER BY dept_name";

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

    /** Fetches a single department by primary key. */
    public Department getDepartmentById(int departmentId) {
        String sql = "SELECT department_id, dept_name, dept_code, contact_person, created_at "
                   + "FROM Departments WHERE department_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts a new department.
     *
     * @param dept the {@link Department} to persist (dept_name and dept_code are required)
     * @return {@code true} if the insert succeeded
     */
    public boolean addDepartment(Department dept) {
        String sql = "INSERT INTO Departments (dept_name, dept_code, contact_person) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dept.getDeptName());
            ps.setString(2, dept.getDeptCode());
            ps.setString(3, dept.getContactPerson());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates an existing department record.
     *
     * @param dept the {@link Department} containing updated values; must have a valid departmentId
     * @return {@code true} if a row was updated
     */
    public boolean updateDepartment(Department dept) {
        String sql = "UPDATE Departments SET dept_name = ?, dept_code = ?, contact_person = ? "
                   + "WHERE department_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dept.getDeptName());
            ps.setString(2, dept.getDeptCode());
            ps.setString(3, dept.getContactPerson());
            ps.setInt(4, dept.getDepartmentId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a department by primary key.
     *
     * @param departmentId the ID of the department to delete
     * @return {@code true} if a row was deleted
     */
    public boolean deleteDepartment(int departmentId) {
        String sql = "DELETE FROM Departments WHERE department_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, departmentId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Maps the current {@link ResultSet} row to a {@link Department} object. */
    private Department mapRow(ResultSet rs) throws SQLException {
        Department d = new Department();
        d.setDepartmentId(rs.getInt("department_id"));
        d.setDeptName(rs.getString("dept_name"));
        d.setDeptCode(rs.getString("dept_code"));
        d.setContactPerson(rs.getString("contact_person"));
        d.setCreatedAt(rs.getTimestamp("created_at"));
        return d;
    }
}
