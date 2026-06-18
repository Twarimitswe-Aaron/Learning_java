package lms;

import lms.concurrent.BorrowTask;
import lms.dao.DatabaseConnection;
import lms.models.Book;
import lms.service.LibraryService;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final LibraryService libraryService = new LibraryService();

    public static void main(String[] args) {
        System.out.println("=== Library Management System ===");

        // Initialize Database Schema and sample data
        DatabaseConnection.initializeDatabase();
        seedData();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Login as Librarian");
            System.out.println("2. Login as Borrower");
            System.out.println("3. Run Concurrent Simulation (Automated Test)");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            String choiceStr = scanner.nextLine();
            int choice = -1;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    librarianMenu(scanner);
                    break;
                case 2:
                    borrowerMenu(scanner);
                    break;
                case 3:
                    runSimulation();
                    break;
                case 4:
                    System.out.println("Exiting the system. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select an option from 1 to 4.");
            }
        }
        scanner.close();
    }

    private static void librarianMenu(Scanner scanner) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- Librarian Menu ---");
            System.out.println("1. List Available Books");
            System.out.println("2. Logout");
            System.out.print("Enter your choice: ");

            String choiceStr = scanner.nextLine();
            int choice = -1;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    listBooks();
                    break;
                case 2:
                    loggedIn = false;
                    System.out.println("Librarian logged out.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void borrowerMenu(Scanner scanner) {
        System.out.print("Enter your Member ID to login (e.g., 1, 2, or 3): ");
        int memberId;
        try {
            memberId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Member ID must be a number.");
            return; // go back to main menu
        }

        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- Borrower Menu (Member ID: " + memberId + ") ---");
            System.out.println("1. List Available Books");
            System.out.println("2. Borrow a Book");
            System.out.println("3. Return a Book");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            String choiceStr = scanner.nextLine();
            int choice = -1;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    listBooks();
                    break;
                case 2:
                    borrowBookInteractive(scanner, memberId);
                    break;
                case 3:
                    returnBookInteractive(scanner, memberId);
                    break;
                case 4:
                    loggedIn = false;
                    System.out.println("Borrower logged out.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void listBooks() {
        System.out.println("\n--- Available Books ---");
        List<Book> availableBooks = libraryService.listAvailableBooks();
        if (availableBooks.isEmpty()) {
            System.out.println("No books currently available.");
        } else {
            for (Book book : availableBooks) {
                System.out.println(book);
            }
        }
    }

    private static void borrowBookInteractive(Scanner scanner, int memberId) {
        System.out.print("Enter Book ISBN to borrow (e.g. ISBN-001): ");
        String isbn = scanner.nextLine();

        boolean success = libraryService.borrowBook(memberId, isbn);
        if (success) {
            System.out.println("--> Successfully borrowed book " + isbn + " for Member " + memberId);
        } else {
            System.out.println("--> Failed to borrow book.");
        }
    }

    private static void returnBookInteractive(Scanner scanner, int memberId) {
        System.out.print("Enter Book ISBN to return (e.g. ISBN-001): ");
        String isbn = scanner.nextLine();

        boolean success = libraryService.returnBook(memberId, isbn);
        if (success) {
            // Service prints its own message
        } else {
            System.out.println("--> Failed to return book.");
        }
    }

    private static void runSimulation() {
        System.out.println("\n--- Starting Concurrent Borrowing Simulation ---");
        ExecutorService executor = Executors.newFixedThreadPool(5); // 5 librarians

        Runnable[] tasks = {
            new BorrowTask(libraryService, 1, 1, "ISBN-001"),
            new BorrowTask(libraryService, 2, 1, "ISBN-002"),
            new BorrowTask(libraryService, 3, 1, "ISBN-003"),
            new BorrowTask(libraryService, 4, 1, "ISBN-004"),
            new BorrowTask(libraryService, 5, 1, "ISBN-005"),
            new BorrowTask(libraryService, 1, 1, "ISBN-006"), // Should fail (limit)
            new BorrowTask(libraryService, 2, 2, "ISBN-007"), // Race condition
            new BorrowTask(libraryService, 3, 3, "ISBN-007")  // Race condition
        };

        for (Runnable task : tasks) {
            executor.submit(task);
        }

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n--- Simulation Complete ---");
    }

    private static void seedData() {
        String clearBooks = "DELETE FROM books;";
        String clearMembers = "DELETE FROM members;";
        String clearRecords = "DELETE FROM borrowing_records;";
        
        String insertBooks = "INSERT INTO books (isbn, title, author, publication_year, is_available) VALUES " +
                "('ISBN-001', 'Java Programming', 'Author A', 2020, 1)," +
                "('ISBN-002', 'Data Structures', 'Author B', 2021, 1)," +
                "('ISBN-003', 'Database Systems', 'Author C', 2019, 1)," +
                "('ISBN-004', 'Operating Systems', 'Author D', 2018, 1)," +
                "('ISBN-005', 'Computer Networks', 'Author E', 2022, 1)," +
                "('ISBN-006', 'Software Engineering', 'Author F', 2023, 1)," +
                "('ISBN-007', 'Machine Learning', 'Author G', 2021, 1)";

        String insertMembers = "INSERT INTO members (member_id, name) VALUES " +
                "(1, 'Alice')," +
                "(2, 'Bob')," +
                "(3, 'Charlie')";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Clean slate for testing
            stmt.execute(clearRecords);
            stmt.execute(clearBooks);
            stmt.execute(clearMembers);

            // Insert fresh data
            stmt.execute(insertBooks);
            stmt.execute(insertMembers);

        } catch (SQLException e) {
            System.err.println("Error seeding data: " + e.getMessage());
        }
    }
}
