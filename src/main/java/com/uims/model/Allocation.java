package com.uims.model;

import java.sql.Timestamp;

/**
 * Allocation – records an item being issued from the store to a department.
 *
 * Corresponds to the <code>Allocation</code> table in the database.
 */
public class Allocation {

    private int       allocationId;
    private int       itemId;
    private int       departmentId;
    private int       quantityIssued;
    private int       issuedBy;       // FK to Users.user_id
    private Timestamp issueDate;
    private String    remarks;

    // Denormalised display fields (populated by JOIN queries – not stored in DB)
    private String    itemName;
    private String    deptName;
    private String    issuedByName;

    /** Default constructor required by some frameworks. */
    public Allocation() { }

    // Getters and setters

    public int getAllocationId() { return allocationId; }
    public void setAllocationId(int allocationId) { this.allocationId = allocationId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    public int getQuantityIssued() { return quantityIssued; }
    public void setQuantityIssued(int quantityIssued) { this.quantityIssued = quantityIssued; }

    public int getIssuedBy() { return issuedBy; }
    public void setIssuedBy(int issuedBy) { this.issuedBy = issuedBy; }

    public Timestamp getIssueDate() { return issueDate; }
    public void setIssueDate(Timestamp issueDate) { this.issueDate = issueDate; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    // Denormalised display fields

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public String getIssuedByName() { return issuedByName; }
    public void setIssuedByName(String issuedByName) { this.issuedByName = issuedByName; }

    @Override
    public String toString() {
        return "Allocation{allocationId=" + allocationId + ", itemId=" + itemId
               + ", departmentId=" + departmentId + ", quantityIssued=" + quantityIssued + "}";
    }
}
