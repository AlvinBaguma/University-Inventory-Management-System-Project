-- =============================================================
--  University Inventory Management System – Database Schema
--  Database : MySQL 5.7+
--  Run this script once to initialise the schema and seed data.
-- =============================================================

-- Create (and select) the database
CREATE DATABASE IF NOT EXISTS university_inventory
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE university_inventory;

-- -------------------------------------------------------------
--  Table: Users
--  Stores login credentials and roles for all system users.
--  Roles: 'Administrator' or 'Storekeeper'
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Users (
    user_id       INT            NOT NULL AUTO_INCREMENT,
    username      VARCHAR(50)    NOT NULL UNIQUE,
    password      VARCHAR(255)   NOT NULL,   -- store hashed passwords in production
    full_name     VARCHAR(100)   NOT NULL,
    role          ENUM('Administrator', 'Storekeeper') NOT NULL DEFAULT 'Storekeeper',
    created_at    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------------
--  Table: Departments
--  University departments that can receive inventory items.
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Departments (
    department_id   INT           NOT NULL AUTO_INCREMENT,
    dept_name       VARCHAR(100)  NOT NULL UNIQUE,
    dept_code       VARCHAR(20)   NOT NULL UNIQUE,
    contact_person  VARCHAR(100),
    created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (department_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------------
--  Table: Inventory
--  Master list of all inventory items tracked by the system.
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Inventory (
    item_id       INT            NOT NULL AUTO_INCREMENT,
    item_name     VARCHAR(150)   NOT NULL,
    category      VARCHAR(100)   NOT NULL,
    quantity      INT            NOT NULL DEFAULT 0,
    unit          VARCHAR(30)    NOT NULL DEFAULT 'piece',
    description   TEXT,
    date_added    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    added_by      INT,                         -- FK to Users
    PRIMARY KEY (item_id),
    CONSTRAINT fk_inventory_user
        FOREIGN KEY (added_by) REFERENCES Users (user_id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------------
--  Table: Allocation
--  Records each time an inventory item is issued to a department.
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Allocation (
    allocation_id   INT       NOT NULL AUTO_INCREMENT,
    item_id         INT       NOT NULL,
    department_id   INT       NOT NULL,
    quantity_issued INT       NOT NULL DEFAULT 1,
    issued_by       INT,                       -- FK to Users (Storekeeper)
    issue_date      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    remarks         TEXT,
    PRIMARY KEY (allocation_id),
    CONSTRAINT fk_allocation_item
        FOREIGN KEY (item_id)       REFERENCES Inventory   (item_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_allocation_dept
        FOREIGN KEY (department_id) REFERENCES Departments (department_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_allocation_user
        FOREIGN KEY (issued_by)     REFERENCES Users       (user_id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================
--  Seed data – default administrator account
--  Password stored here is plain text for demo purposes only.
--  In production, store a BCrypt/SHA-256 hash instead.
-- =============================================================
INSERT IGNORE INTO Users (username, password, full_name, role)
VALUES ('admin', 'admin123', 'System Administrator', 'Administrator');

INSERT IGNORE INTO Users (username, password, full_name, role)
VALUES ('storekeeper', 'store123', 'Default Storekeeper', 'Storekeeper');

-- Sample departments
INSERT IGNORE INTO Departments (dept_name, dept_code, contact_person)
VALUES
    ('Computer Science',        'CS',   'Dr. Jane Smith'),
    ('Electrical Engineering',  'EE',   'Prof. John Doe'),
    ('Library Services',        'LIB',  'Ms. Alice Brown'),
    ('Administration Office',   'ADMIN','Mr. Robert White');

-- Sample inventory items
INSERT IGNORE INTO Inventory (item_name, category, quantity, unit, description, added_by)
VALUES
    ('Dell Laptop',          'Electronics',  10, 'piece',  'Dell Inspiron 15 laptops for computer labs',         1),
    ('Office Chair',         'Furniture',    25, 'piece',  'Ergonomic office chairs',                            1),
    ('Whiteboard Marker',    'Stationery',  100, 'box',    'Blue and black whiteboard markers',                  2),
    ('Projector',            'Electronics',   5, 'piece',  'Epson HD projectors for lecture rooms',              1),
    ('Desk',                 'Furniture',    15, 'piece',  'Wooden study desks',                                 2);
