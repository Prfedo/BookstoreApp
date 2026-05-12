# Bookish — Desktop Bookstore App

A desktop bookstore application built with **Java Swing** and **SQLite** for a team university project.

![App Screenshot](BookstoreApp/pics/screenshot.png)

---

## Team Members

| # | Name | Role | Key files |
|---|------|------|-----------|
| 1 | Esmail Mohamed | Team lead + models | `Main.java`, `Book.java`, `User.java`, `Order.java`, `CartItem.java`, `SessionManager.java` |
| 2 | Malak Medhat | Database layer | `DatabaseManager.java`, `BookDAO.java`, `UserDAO.java`, `OrderDAO.java`, `ConnectSQL.java` |
| 3 | Habiba Ahmed | Book catalog UI | `MainWindow.java`, `CatalogPanel.java`, `BookDetailDialog.java` |
| 4 | Bassel Saeed | Cart, checkout & orders UI | `CartPanel.java`, `CheckOutPanel.java`, `OrderConfirmDialogue.java`, `OrderHistoryPanel.java` |
| 5 | Marwan Mohamed | Login, register & admin | `LoginPanel.java`, `SigninPanel.java`, `AdminPanel.java` |

---

## Features

### Shopping

- Book catalog with cover images, titles, authors, genres, and prices
- Live search by title and genre filter controls
- Book detail dialog with quantity capped by stock
- Shopping cart: add, remove, and clear

### Checkout

- Delivery details with validation
- Cash on delivery, credit card, and debit card options
- Card number and CVV field limits
- Order confirmation with line items
- Per-user order history

### Authentication

- Registration: name, username, email, password
- Login with **username or email**
- Session handling while navigating
- Logout

### Admin panel

- Full book inventory table
- Add books with validation
- Delete books with confirmation
- Stock decreases when orders are placed

---

## Architecture

Four-tier style: **Swing UI → controllers → DAOs → SQLite**.

```text
View (Swing panels)
  ↔
Controller
  ↔
DAO (SQL)
  ↔
SQLite
```

---

## Repository layout

The git root contains this README and a **NetBeans project folder** (open the inner folder in the IDE).

```text
BookstoreApp/                          ← git repository root
├── README.md
└── BookstoreApp/                      ← open this directory in NetBeans
    ├── src/
    │   ├── Main.java
    │   └── com/bookstore/
    │       ├── model/
    │       │   ├── Book.java
    │       │   ├── User.java
    │       │   ├── Order.java
    │       │   ├── CartItem.java
    │       │   └── SessionManager.java
    │       ├── database/
    │       │   ├── ConnectSQL.java
    │       │   ├── DatabaseManager.java
    │       │   ├── BookDAO.java
    │       │   ├── UserDAO.java
    │       │   ├── OrderDAO.java
    │       │   └── JDBC SQLite/
    │       │       └── sqlite-jdbc-3.51.3.0.jar
    │       ├── controller/
    │       │   ├── BookController.java
    │       │   ├── CartController.java
    │       │   ├── UserController.java
    │       │   └── OrderController.java
    │       └── view/
    │           ├── MainWindow.java
    │           ├── CatalogPanel.java
    │           ├── BookDetailDialog.java
    │           ├── CartPanel.java
    │           ├── CheckOutPanel.java
    │           ├── OrderConfirmDialogue.java
    │           ├── OrderHistoryPanel.java
    │           ├── LoginPanel.java
    │           ├── SigninPanel.java
    │           └── AdminPanel.java
    ├── pics/
    │   ├── ICON.png
    │   ├── Appicon.jpg
    │   └── books_cover/
    ├── bookstore.db                    ← created at runtime (see .gitignore)
    ├── build.xml
    └── nbproject/
```

Paths like `pics/...` are resolved from the **process working directory** (usually the NetBeans project folder above), so keep the `pics` folder next to `src` as shown.

---

## Database schema

Tables are created on startup (see `DatabaseManager`).

| Table | Main columns |
|-------|----------------|
| `Books` | `ID`, `Title`, `Author`, `Genre`, `Price`, `Stock`, `Cover` |
| `Users` | `ID`, `Name`, `username`, `email`, `password`, `is_admin` |
| `Orders` | `ID`, `user_id`, `total_price`, `created_at` |
| `Order_item` | `ID`, `order_id`, `book_id`, `quantity`, `price` |

---

## Getting started

### Prerequisites

- **JDK** — Java 17 or newer (this repo’s NetBeans metadata may target a newer release; check `BookstoreApp/nbproject/project.properties` for `javac.source` / `javac.target` and match your installed JDK or adjust in the IDE)
- **NetBeans** (recommended) with Ant
- **SQLite JDBC** — referenced from  
  `BookstoreApp/src/com/bookstore/database/JDBC SQLite/sqlite-jdbc-3.51.3.0.jar`  
  Ensure that file exists and is on the project classpath (already wired in `project.properties`).

### Run the app

1. Open **`BookstoreApp/BookstoreApp`** in NetBeans (the inner project folder).
2. Confirm the SQLite JDBC `.jar` is listed under **Libraries**.  
   If the project fails to compile on your machine, open **Project Properties → Libraries** and remove any teammate-specific absolute paths (see `nbproject/project.properties`).
3. Run **`Main.java`**. Tables are created on first launch; `bookstore.db` appears in the working directory (typically the project folder).

### Create an admin account

1. Register a user in the app.
2. Open `bookstore.db` in any SQLite tool and run:

   ```sql
   UPDATE Users SET is_admin = 1 WHERE email = 'your@email.com';
   ```

3. Restart the app and sign in — the **Admin** entry in the UI becomes available.

---

## Tech stack

| Technology | Role |
|------------|------|
| Java | Application language |
| Java Swing | Desktop UI |
| SQLite | Embedded database |
| JDBC (`sqlite-jdbc`) | Driver |
| NetBeans / Ant | IDE and build |

---

## Notes

- **Java 17+ native-access warning** — you may see:
  `System::load has been called by org.sqlite.SQLiteJDBCLoader...`  
  It is usually harmless. To silence it, add the VM option:
  `--enable-native-access=ALL-UNNAMED`
- **Passwords** are stored in plain text — demo / coursework only.
- **SQL** in DAOs uses string concatenation — not suitable for production or exposed services.

---

## License

Educational use — university team project.
