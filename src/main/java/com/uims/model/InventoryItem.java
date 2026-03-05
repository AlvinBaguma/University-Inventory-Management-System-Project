package com.uims.model;

import java.sql.Timestamp;

/**
 * InventoryItem – represents a single item in the university's inventory.
 *
 * Corresponds to the <code>Inventory</code> table in the database.
 */
public class InventoryItem {

    private int       itemId;
    private String    itemName;
    private String    category;
    private int       quantity;
    private String    unit;
    private String    description;
    private Timestamp dateAdded;
    private int       addedBy;      // FK to Users.user_id

    /** Default constructor required by some frameworks. */
    public InventoryItem() { }

    public InventoryItem(String itemName, String category, int quantity, String unit, String description) {
        this.itemName    = itemName;
        this.category    = category;
        this.quantity    = quantity;
        this.unit        = unit;
        this.description = description;
    }

    // Getters and setters

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getDateAdded() { return dateAdded; }
    public void setDateAdded(Timestamp dateAdded) { this.dateAdded = dateAdded; }

    public int getAddedBy() { return addedBy; }
    public void setAddedBy(int addedBy) { this.addedBy = addedBy; }

    @Override
    public String toString() {
        return "InventoryItem{itemId=" + itemId + ", itemName='" + itemName
               + "', category='" + category + "', quantity=" + quantity + "}";
    }
}
