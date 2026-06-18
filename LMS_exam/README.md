# Library Management System (LMS)

Twarimitswe Aaron
Year 2 B

A robust, interactive Library Management System built using **Java 17**, **Maven**, and **SQLite**. This project demonstrates core Object-Oriented Programming (OOP) principles, relational database persistence using the standard `java.sql` (JDBC) API, custom global exception handling, and multithreading using the `ExecutorService` framework.

## Features
- **Interactive CLI Interface**: A fully interactive, menu-driven command-line interface featuring role-based access for Librarians and Borrowers.
- **Robust Input Validation**: Includes continuous validation loops and regex (e.g., 4-digit year format, ISBN format constraints) that prevent application crashes and user frustration.
- **Custom Global Exception Handling**: Uses a custom `lms.exception` package (`LibraryException`, `UserNotFoundException`, `BookNotFoundException`) to handle business logic errors gracefully without crashing or exposing database internals to the user.
- **Data Persistence (JDBC)**: Utilizes `java.sql` components to safely execute queries, track borrowing transactions, and persistently save book and member records into an SQLite database (`library.db`). The database schema strictly enforces referential integrity (`PRAGMA foreign_keys = ON`).
- **Comprehensive CRUD Operations**: Librarians can fully Create, Read, Update, and Delete both Books and Members safely.
- **Multithreading & Thread Safety**: Features an automated concurrent simulation using `ExecutorService` to simulate multiple librarians processing book requests simultaneously. Strict thread-safety is enforced via Java `synchronized` blocks and SQLite database transactions to prevent race conditions.
- **Business Logic Constraints**: Enforces limits such as preventing a member from borrowing more than 5 books, and ensuring a member cannot return a book they didn't borrow.

## Prerequisites
To run this project, you will need:
- **Java Development Kit (JDK) 11** or higher (configured in `pom.xml` for Java 17).
- **Apache Maven** installed on your system.
- An internet connection (only for the first run, so Maven can download the `sqlite-jdbc` driver).

## Setup & Execution

### 1. Navigate to the project directory
Open your terminal and navigate to the project directory:
```bash
cd "/media/newvolume/Documents/Notes2/JAVA/programs/This Year/LMS_exam"
```

### 2. Compile and Run
You can compile and run the project using a simple Maven command:
```bash
mvn compile exec:java
```

### What Happens When You Run It?
When you execute the program, the `Main` class will:
1. Initialize the SQLite database schema (`books`, `members`, `borrowing_records` tables) if it doesn't already exist.
2. Launch the **Main Menu**, prompting you to login as a Librarian or Borrower, or register as a new Member.
3. Allow you to interactively perform Library tasks, safely saving all changes across application restarts.
4. Provide an option to run the concurrent multi-threaded simulation.

## Project Structure
- `src/main/java/lms/Main.java`: The interactive console UI and entry point.
- `src/main/java/lms/models/`: Contains the `Book` and `Member` data models.
- `src/main/java/lms/dao/DatabaseConnection.java`: Manages the database connection and schema initialization.
- `src/main/java/lms/service/LibraryService.java`: Houses the core JDBC queries, business logic, and synchronized transactions.
- `src/main/java/lms/exception/`: Contains the custom global exception hierarchy for safe error routing.
- `src/main/java/lms/concurrent/BorrowTask.java`: A `Runnable` task that handles automated borrowing requests across threads for the concurrent simulation.
