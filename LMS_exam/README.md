# Library Management System (LMS)

A simplified Library Management System built using **Java 17**, **Maven**, and **SQLite**. This project demonstrates core Object-Oriented Programming (OOP) principles, multithreading using the `ExecutorService` framework, and database persistence using the standard `java.sql` (JDBC) API.

## Features
- **Object-Oriented Encapsulation**: Clean encapsulation of `Book` and `Member` data models.
- **Multithreading & Thread Safety**: Uses the `ExecutorService` to simulate multiple librarians processing book requests concurrently. Strict thread-safety is enforced via Java `synchronized` blocks and SQLite database transactions to prevent race conditions (e.g., preventing two librarians from checking out the exact same book simultaneously).
- **Business Logic Constraints**: Enforces a strict limit where a single member cannot borrow more than 5 books at a given time.
- **Data Persistence (JDBC)**: Utilizes `java.sql` components (`Connection`, `Statement`, `PreparedStatement`, `ResultSet`) to safely execute queries, track borrowing transactions, and update book availability.

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
1. Automatically create a local `library.db` SQLite database file in the project root.
2. Initialize the database schema (`books`, `members`, `borrowing_records` tables).
3. Seed the database with sample data.
4. Launch a multithreaded simulation using `ExecutorService` with 5 threads.
5. Simulate concurrent borrowing scenarios, including testing the 5-book limit and race conditions.

## Project Structure
- `src/main/java/lms/Main.java`: The main entry point and simulation runner.
- `src/main/java/lms/models/`: Contains the `Book` and `Member` POJO classes.
- `src/main/java/lms/dao/DatabaseConnection.java`: Manages the `java.sql.Connection` and database initialization.
- `src/main/java/lms/service/LibraryService.java`: Houses the core JDBC queries and synchronized borrowing logic.
- `src/main/java/lms/concurrent/BorrowTask.java`: A `Runnable` task that handles individual borrowing requests across threads.
