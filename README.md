# Bookstore App

A desktop bookstore management and shopping application built with **Java Swing** and **SQLite**.

This project is designed as a team-based university project with clear module ownership (models, database, UI, cart/orders, and authentication/admin), then integrated into one working desktop app.

---

## Team Members

- Malak Medhat
- Habiba Ahmed
- Marwan Mohamed
- Esmail Mohamed
- Bassel Saeed

---

## Overview

Bookstore App provides a modern catalog interface with searchable books, category filtering, user management, and order persistence through SQLite.

### Current Highlights

- Responsive desktop UI built with Swing
- Book catalog with:
  - live title search
  - genre filtering
  - book detail dialog
- SQLite-backed data layer with DAO pattern:
  - `BookDAO`
  - `UserDAO`
  - `OrderDAO`
- Automatic table initialization on app startup
- Local image-based branding and cover rendering

---

## Project Structure

```text
BookstoreApp/
├── src/
│   ├── Main.java
│   └── com/bookstore/
│       ├── controller/
│       ├── database/
│       ├── model/
│       └── view/
├── pics/
│   ├── ICON.png
│   ├── Appicon.jpg
│   └── books_cover/
├── JDBC SQLite/
│   └── sqlite-jdbc-3.51.3.0.jar
├── bookstore.db
└── README.md
```

---

## Technology Stack

- **Language:** Java
- **UI Framework:** Java Swing
- **Database:** SQLite
- **Database Access:** JDBC (`sqlite-jdbc`)
- **Build/IDE:** NetBeans (Ant project)

---

## Core Modules

### 1) Model Layer

Contains the main domain entities:

- `Book`
- `User`
- `Order`
- `CartItem`

### 2) Database Layer

Located in `src/com/bookstore/database/`:

- `ConnectSQL` - database connection provider
- `DatabaseManager` - table creation and SQL resource cleanup
- `BookDAO` - book CRUD + search
- `UserDAO` - registration, login, and user lookup
- `OrderDAO` - order persistence and order history retrieval

### 3) View Layer

Located in `src/com/bookstore/view/`:

- `MainWindow` - application frame
- `CatalogPanel` - catalog browsing, filtering, and details dialog
- `BookDetailDialog` - selected book details + add-to-cart interaction

---

## Getting Started

### Prerequisites

- Java JDK (recommended: Java 17+)
- NetBeans IDE (recommended for this project structure)
- SQLite JDBC driver jar in `JDBC SQLite/`

### Run in NetBeans

1. Open the project folder in NetBeans.
2. Ensure SQLite JDBC library is available in project libraries.
3. Run `Main.java`.

On startup, the app calls `DatabaseManager.createTabels()` to make sure required tables exist.

---

## Database Notes

- Default SQLite file: `bookstore.db`
- Main tables:
  - `Books`
  - `Users`
  - `Orders`
  - `Order_item`

Book cover paths are resolved from filenames and displayed from:

`pics/books_cover/`

---

## Team Workflow (Recommended)

- Use feature branches (`member2-database`, `member3-catalog`, etc.)
- Keep commits focused and descriptive
- Open PRs for review before merging to `main`
- Avoid committing generated files (`build/`, `.class`, IDE private metadata)

---

## Known Warnings

You may see this runtime warning with recent Java versions:

`System::load has been called by org.sqlite.SQLiteJDBCLoader ...`

This is a Java native-access warning from SQLite JDBC and does not block normal execution.

Optional VM argument to silence it:

`--enable-native-access=ALL-UNNAMED`

---

## License

This project is developed for educational purposes.
