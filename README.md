# OOP2 Final Project: JavaFX Point of Sale System

A desktop Point of Sale (POS) application built with JavaFX and MySQL, made as the final project for the OOP2 (Object-Oriented Programming 2) university course.

## Features

- Role-based login, with separate flows for Admins and Cashiers
- Admin dashboard
  - Manage Admins and Cashiers (employee accounts)
  - Manage Product Categories
  - Manage Customers
  - Manage Products
  - View Sales Reports
- Cashier dashboard
  - Search products
  - Add items to cart
  - Checkout and complete sales

## Tech Stack

- Java 23
- JavaFX 17 (Controls + FXML)
- BootstrapFX (styling)
- MySQL (via `mysql-connector-java`)
- Maven

## Getting Started

### Prerequisites

- JDK 23
- Maven
- A running MySQL server

### Database setup

1. Create a MySQL database (e.g. `OOP2_Project`) with the tables used by this app: `admins`, `cashiers`, `categories`, `customers`, `products`, `sales`, `sale_items`.
2. Copy the example config and fill in your own credentials:

```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

Then edit `src/main/resources/db.properties` with your local database URL, username, and password. This file is git-ignored and will not be committed.

### Run the app

```bash
mvn clean javafx:run
```

## Project Structure

```
src/main/java/org/example/oop2_finalproject/
├── controllers/       # FXML controllers
├── Database_Service/  # DB connection + repository classes (data access layer)
├── models/            # Domain models (Admin, Cashier, Product, Sale, ...)
├── views/             # JavaFX views (Admin/Cashier dashboards, login, etc.)
└── utils/             # Shared utilities (e.g. cart state)
```

## Notes

Built as coursework to practice OOP principles (encapsulation, inheritance, and separation of concerns between UI, controllers, and data access) in a real desktop application.
