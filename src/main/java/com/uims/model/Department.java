package com.uims.model;

import java.sql.Timestamp;

/**
 * Department – represents a university department.
 *
 * Corresponds to the <code>Departments</code> table in the database.
 */
public class Department {

    private int       departmentId;
    private String    deptName;
    private String    deptCode;
    private String    contactPerson;
    private Timestamp createdAt;

    /** Default constructor required by some frameworks. */
    public Department() { }

    public Department(String deptName, String deptCode, String contactPerson) {
        this.deptName      = deptName;
        this.deptCode      = deptCode;
        this.contactPerson = contactPerson;
    }

    // Getters and setters

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public String getDeptCode() { return deptCode; }
    public void setDeptCode(String deptCode) { this.deptCode = deptCode; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Department{departmentId=" + departmentId + ", deptName='" + deptName
               + "', deptCode='" + deptCode + "'}";
    }
}
