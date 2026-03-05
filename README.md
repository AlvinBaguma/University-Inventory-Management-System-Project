# University Inventory Management System (UIMS)

A web-based University Inventory Management System built with **Java Servlets, JSP, MySQL, and Apache Tomcat**, following the **MVC design pattern**.

## Features

| Module | Functionality |
|---|---|
| **Authentication** | Login / logout for Administrator and Storekeeper roles |
| **Inventory** | Add, view, update, delete inventory items |
| **Department Allocation** | Issue items to departments; view issued items |
| **Dashboard** | Role-aware landing page with quick-action links |

> **Placeholder modules** for future development: Reports, Department Management, User Management, Advanced Features (barcode scanning, email alerts, audit trail).

---

## Technology Stack

| Layer | Technology |
|---|---|
| Backend | Java 11, Java Servlets (javax.servlet 4.0) |
| View | JSP 2.3, JSTL 1.2 |
| Database | MySQL 5.7+ |
| Server | Apache Tomcat 9.x |
| Build | Apache Maven 3.6+ |

---

## Project Structure

```
UniversityInventorySystem/
├── pom.xml                          ← Maven build file
├── database.sql                     ← Database schema and seed data
└── src/
    └── main/
        ├── java/com/uims/
        │   ├── db/
        │   │   └── DBConnection.java       ← Database connection helper
        │   ├── model/
        │   │   ├── User.java
        │   │   ├── InventoryItem.java
        │   │   ├── Department.java
        │   │   └── Allocation.java
        │   ├── dao/
        │   │   ├── UserDAO.java
        │   │   ├── InventoryDAO.java
        │   │   ├── DepartmentDAO.java
        │   │   └── AllocationDAO.java
        │   └── controller/
        │       ├── LoginServlet.java
        │       ├── LogoutServlet.java
        │       ├── DashboardServlet.java
        │       ├── InventoryServlet.java
        │       └── AllocationServlet.java
        └── webapp/
            ├── WEB-INF/web.xml
            ├── css/style.css
            ├── index.jsp
            ├── login.jsp
            ├── dashboard.jsp
            ├── error.jsp
            ├── inventory/
            │   ├── list.jsp
            │   ├── add.jsp
            │   └── edit.jsp
            └── allocation/
                ├── list.jsp
                └── issue.jsp
```

---

## Setup Instructions

### Prerequisites

- Java JDK 11 or higher
- Apache Maven 3.6+
- MySQL 5.7+ (or MariaDB 10.3+)
- Apache Tomcat 9.x

### 1. Clone the repository

```bash
git clone <repository-url>
cd UniversityInventorySystem
```

### 2. Create the database

Open MySQL and run:

```bash
mysql -u root -p < database.sql
```

This will:
- Create the `university_inventory` database
- Create four tables: `Users`, `Departments`, `Inventory`, `Allocation`
- Insert seed data including default admin and storekeeper accounts

### 3. Configure the database connection

Open `src/main/java/com/uims/db/DBConnection.java` and update:

```java
private static final String DB_URL      = "jdbc:mysql://localhost:3306/university_inventory...";
private static final String DB_USERNAME = "root";       // your MySQL username
private static final String DB_PASSWORD = "yourpassword"; // your MySQL password
```

### 4. Build the WAR file

```bash
mvn clean package
```

The built WAR file will be at `target/UniversityInventorySystem.war`.

### 5. Deploy to Tomcat

Copy the WAR file to Tomcat's webapps directory:

```bash
cp target/UniversityInventorySystem.war /path/to/tomcat/webapps/
```

Start Tomcat:

```bash
/path/to/tomcat/bin/startup.sh      # Linux/macOS
/path/to/tomcat/bin/startup.bat     # Windows
```

### 6. Access the application

Open your browser and navigate to:

```
http://localhost:8080/UniversityInventorySystem/
```

---

## Default Login Credentials

| Role | Username | Password |
|---|---|---|
| Administrator | `admin` | `admin123` |
| Storekeeper | `storekeeper` | `store123` |

> **⚠️ Security Note:** Change these credentials immediately after your first login. In a production environment, passwords should be hashed (e.g., using BCrypt) rather than stored as plain text.

---

## Database Schema

### Users
Stores user credentials and roles (`Administrator` or `Storekeeper`).

### Departments
University departments that receive inventory items (e.g., Computer Science, Library).

### Inventory
Master list of all items with name, category, quantity, unit, and description.

### Allocation
Tracks every time an item is issued to a department, including the quantity, date, issuing user, and remarks. Issuing an item automatically decrements its quantity in `Inventory` within a single database transaction.

---

## Future Modules (Planned)

- **Reports** – PDF/Excel export, department usage statistics, low-stock alerts
- **Department Management** – Add/edit/remove departments through the UI
- **User Management** – Admin UI to manage system users and reset passwords
- **Advanced Features** – Barcode/QR scanning, email notifications, audit trail

