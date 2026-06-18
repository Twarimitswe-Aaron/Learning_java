package lms;

import lms.concurrent.BorrowTask;
import lms.dao.DatabaseConnection;
import lms.models.Book;
import lms.models.Member;
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
            System.out.println("├── 1. Login as Librarian");
            System.out.println("├── 2. Login as Borrower");
            System.out.println("├── 3. Register as New Borrower");
            System.out.println("├── 4. Run Concurrent Simulation (Automated Test)");
            System.out.println("└── 5. Exit");
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
                    registerBorrowerInteractive(scanner);
                    break;
                case 4:
                    runSimulation();
                    break;
                case 5:
                    System.out.println("Exiting the system. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select an option from 1 to 5.");
            }
        }
        scanner.close();
    }

    private static void librarianMenu(Scanner scanner) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n└── Librarian Menu");
            System.out.println("    ├── 1. Book Management");
            System.out.println("    ├── 2. Member Management");
            System.out.println("    └── 3. Logout");
            System.out.print("    Enter your choice: ");

            String choiceStr = scanner.nextLine();
            int choice = -1;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("    Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    bookManagementMenu(scanner);
                    break;
                case 2:
                    memberManagementMenu(scanner);
                    break;
                case 3:
                    loggedIn = false;
                    System.out.println("    Librarian logged out.");
                    break;
                default:
                    System.out.println("    Invalid choice.");
            }
        }
    }

    private static void bookManagementMenu(Scanner scanner) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n    └── Book Management");
            System.out.println("        ├── 1. Add New Book (Create)");
            System.out.println("        ├── 2. List All Books (Read)");
            System.out.println("        ├── 3. Update Book (Update)");
            System.out.println("        ├── 4. Delete Book (Delete)");
            System.out.println("        └── 5. Back to Librarian Menu");
            System.out.print("        Enter your choice: ");

            String choiceStr = scanner.nextLine();
            int choice = -1;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("        Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    registerBookInteractive(scanner);
                    break;
                case 2:
                    listAllBooksWithBorrower();
                    break;
                case 3:
                    updateBookInteractive(scanner);
                    break;
                case 4:
                    deleteBookInteractive(scanner);
                    break;
                case 5:
                    inMenu = false;
                    break;
                default:
                    System.out.println("        Invalid choice.");
            }
        }
    }

    private static void memberManagementMenu(Scanner scanner) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n    └── Member Management");
            System.out.println("        ├── 1. Register New Member (Create)");
            System.out.println("        ├── 2. List All Members (Read)");
            System.out.println("        ├── 3. Update Member Details (Update)");
            System.out.println("        ├── 4. Delete Member (Delete)");
            System.out.println("        └── 5. Back to Librarian Menu");
            System.out.print("        Enter your choice: ");

            String choiceStr = scanner.nextLine();
            int choice = -1;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("        Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    registerBorrowerInteractive(scanner);
                    break;
                case 2:
                    listMembers();
                    break;
                case 3:
                    updateMemberInteractive(scanner);
                    break;
                case 4:
                    deleteMemberInteractive(scanner);
                    break;
                case 5:
                    inMenu = false;
                    break;
                default:
                    System.out.println("        Invalid choice.");
            }
        }
    }

    private static void borrowerMenu(Scanner scanner) {
        System.out.print("Enter your Name to login: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Invalid input. Name cannot be empty.");
            return; // go back to main menu
        }

        int memberId = libraryService.getMemberIdByName(name);
        if (memberId == -1) {
            System.out.println("User not found. Registration required. Please register first from the Main Menu.");
            return;
        }

        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n└── Borrower Menu (User: " + name + ", ID: " + memberId + ")");
            System.out.println("    ├── 1. List Available Books");
            System.out.println("    ├── 2. List Borrowed Books");
            System.out.println("    ├── 3. Borrow a Book");
            System.out.println("    ├── 4. Return a Book");
            System.out.println("    └── 5. Logout");
            System.out.print("    Enter your choice: ");

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
                    listBorrowedBooks(memberId);
                    break;
                case 3:
                    borrowBookInteractive(scanner, memberId);
                    break;
                case 4:
                    returnBookInteractive(scanner, memberId);
                    break;
                case 5:
                    loggedIn = false;
                    System.out.println("Borrower logged out.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void listBooks() {
        System.out.println("\n        --- Available Books ---");
        List<Book> availableBooks = libraryService.listAvailableBooks();
        if (availableBooks.isEmpty()) {
            System.out.println("        No books currently available.");
        } else {
            System.out.printf("        %-15s | %-25s | %-20s | %-6s | %-10s%n", "ISBN", "Title", "Author", "Year", "Available");
            System.out.println("        -----------------------------------------------------------------------------------------");
            for (Book book : availableBooks) {
                System.out.printf("        %-15s | %-25s | %-20s | %-6d | %-10s%n", 
                        book.getIsbn(), 
                        book.getTitle(), 
                        book.getAuthor(), 
                        book.getPublicationYear(), 
                        book.isAvailable() ? "Yes" : "No");
            }
        }
    }

    private static void borrowBookInteractive(Scanner scanner, int memberId) {
        String isbn;
        while (true) {
            System.out.print("    Enter Book ISBN to borrow (Format: ISBN-XXX, or 'q' to cancel): ");
            isbn = scanner.nextLine().trim();
            if (isbn.equalsIgnoreCase("q")) return;
            if (isbn.isEmpty()) {
                System.out.println("    Validation failed: ISBN cannot be empty.");
                continue;
            }
            if (!isbn.matches("^ISBN-[A-Za-z0-9]+$")) {
                System.out.println("    Validation failed: ISBN must be in the format 'ISBN-XXX' (e.g. ISBN-001).");
                continue;
            }
            if (!libraryService.isBookExists(isbn)) {
                System.out.println("    --> Book with ISBN '" + isbn + "' does not exist. Try again.");
                continue;
            }
            break;
        }

        boolean success = libraryService.borrowBook(memberId, isbn);
        if (success) {
            System.out.println("    --> Successfully borrowed book " + isbn + " for Member " + memberId);
        } else {
            System.out.println("    --> Failed to borrow book.");
        }
    }

    private static void returnBookInteractive(Scanner scanner, int memberId) {
        String isbn;
        while (true) {
            System.out.print("    Enter Book ISBN to return (Format: ISBN-XXX, or 'q' to cancel): ");
            isbn = scanner.nextLine().trim();
            if (isbn.equalsIgnoreCase("q")) return;
            if (isbn.isEmpty()) {
                System.out.println("    Validation failed: ISBN cannot be empty.");
                continue;
            }
            if (!isbn.matches("^ISBN-[A-Za-z0-9]+$")) {
                System.out.println("    Validation failed: ISBN must be in the format 'ISBN-XXX' (e.g. ISBN-001).");
                continue;
            }
            if (!libraryService.isBookExists(isbn)) {
                System.out.println("    --> Book with ISBN '" + isbn + "' does not exist. Try again.");
                continue;
            }
            break;
        }

        boolean success = libraryService.returnBook(memberId, isbn);
        if (success) {
            // Service prints its own message
        } else {
            System.out.println("    --> Failed to return book.");
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

    private static void registerBookInteractive(Scanner scanner) {
        System.out.println("\n        --- Register a New Book ---");
        String isbn;
        while (true) {
            System.out.print("        Enter ISBN (Format: ISBN-XXX, or 'q' to cancel): ");
            isbn = scanner.nextLine().trim();
            if (isbn.equalsIgnoreCase("q")) return;
            if (isbn.isEmpty()) {
                System.out.println("        Validation failed: ISBN cannot be empty.");
                continue;
            }
            if (!isbn.matches("^ISBN-[A-Za-z0-9]+$")) {
                System.out.println("        Validation failed: ISBN must be in the format 'ISBN-XXX' (e.g. ISBN-001).");
                continue;
            }
            if (libraryService.isBookExists(isbn)) {
                System.out.println("        --> Book with ISBN '" + isbn + "' already exists. Try another.");
                continue;
            }
            break;
        }
        
        String title;
        while (true) {
            System.out.print("        Enter Title: ");
            title = scanner.nextLine().trim();
            if (title.isEmpty()) {
                System.out.println("        Validation failed: Title cannot be empty.");
                continue;
            }
            break;
        }
        
        String author;
        while (true) {
            System.out.print("        Enter Author: ");
            author = scanner.nextLine().trim();
            if (author.isEmpty()) {
                System.out.println("        Validation failed: Author cannot be empty.");
                continue;
            }
            break;
        }
        
        int year;
        while (true) {
            System.out.print("        Enter Publication Year (4 digits): ");
            String yearStr = scanner.nextLine().trim();
            if (!yearStr.matches("\\d{4}")) {
                System.out.println("        Validation failed: Year must be exactly 4 digits.");
                continue;
            }
            try {
                year = Integer.parseInt(yearStr);
                break;
            } catch (NumberFormatException e) {
                System.out.println("        Validation failed: Invalid year format.");
            }
        }

        Book book = new Book(isbn, title, author, year, true);
        boolean success = libraryService.registerBook(book);
        if (success) {
            System.out.println("        --> Successfully registered book: " + title);
        } else {
            System.out.println("        --> Failed to register book. (It might already exist)");
        }
    }

    private static void listMembers() {
        System.out.println("\n        --- System Members ---");
        List<Member> members = libraryService.listMembers();
        if (members.isEmpty()) {
            System.out.println("        No members found.");
        } else {
            System.out.printf("        %-10s | %-20s%n", "Member ID", "Name");
            System.out.println("        -----------------------------------");
            for (Member m : members) {
                System.out.printf("        %-10d | %-20s%n", m.getId(), m.getName());
            }
        }
    }

    private static void listBorrowedBooks(int memberId) {
        System.out.println("\n        --- Borrowed Books ---");
        List<Book> borrowedBooks = libraryService.listBorrowedBooks(memberId);
        if (borrowedBooks.isEmpty()) {
            System.out.println("        No borrowed books currently.");
        } else {
            System.out.printf("        %-15s | %-25s | %-20s | %-6s%n", "ISBN", "Title", "Author", "Year");
            System.out.println("        -------------------------------------------------------------------------");
            for (Book book : borrowedBooks) {
                System.out.printf("        %-15s | %-25s | %-20s | %-6d%n",
                        book.getIsbn(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublicationYear());
            }
        }
    }

    private static void registerBorrowerInteractive(Scanner scanner) {
        System.out.println("\n--- Register New Borrower ---");
        String name;
        while (true) {
            System.out.print("Enter your Name (or 'q' to cancel): ");
            name = scanner.nextLine().trim();
            if (name.equalsIgnoreCase("q")) return;
            if (name.isEmpty()) {
                System.out.println("Registration failed: Name cannot be empty. Try again.");
                continue;
            }
            break;
        }

        int newMemberId = libraryService.registerMember(name);
        if (newMemberId != -1) {
            System.out.println("--> Successfully registered! Your new Member ID is: " + newMemberId);
            System.out.println("--> Please use this ID to login as a Borrower.");
        } else {
            System.out.println("--> Registration failed. Please try again.");
        }
    }

    private static void listAllBooksWithBorrower() {
        System.out.println("\n        --- All Books ---");
        List<String[]> books = libraryService.listAllBooksWithBorrower();
        if (books.isEmpty()) {
            System.out.println("        No books found.");
        } else {
            System.out.printf("        %-15s | %-20s | %-15s | %-6s | %-10s | %-15s%n", "ISBN", "Title", "Author", "Year", "Available", "Borrowed By");
            System.out.println("        --------------------------------------------------------------------------------------------------");
            for (String[] b : books) {
                System.out.printf("        %-15s | %-20s | %-15s | %-6s | %-10s | %-15s%n", 
                        b[0], b[1], b[2], b[3], b[4], (b[5] == null ? "None" : b[5]));
            }
        }
    }

    private static void updateBookInteractive(Scanner scanner) {
        System.out.println("\n        --- Update Book ---");
        String isbn;
        while (true) {
            System.out.print("        Enter ISBN of the book to update (Format: ISBN-XXX, or 'q' to cancel): ");
            isbn = scanner.nextLine().trim();
            if (isbn.equalsIgnoreCase("q")) return;
            if (isbn.isEmpty()) {
                System.out.println("        Validation failed: ISBN cannot be empty.");
                continue;
            }
            if (!isbn.matches("^ISBN-[A-Za-z0-9]+$")) {
                System.out.println("        Validation failed: ISBN must be in the format 'ISBN-XXX' (e.g. ISBN-001).");
                continue;
            }
            if (!libraryService.isBookExists(isbn)) {
                System.out.println("        --> Book with ISBN '" + isbn + "' does not exist. Try again.");
                continue;
            }
            break;
        }
        
        System.out.println("        (Press Enter to skip updating a field)");
        System.out.print("        Enter new Title: ");
        String title = scanner.nextLine().trim();
        
        System.out.print("        Enter new Author: ");
        String author = scanner.nextLine().trim();
        
        Integer year = null;
        while (true) {
            System.out.print("        Enter new Publication Year (4 digits, or Enter to skip): ");
            String yearStr = scanner.nextLine().trim();
            if (yearStr.isEmpty()) {
                break;
            }
            if (!yearStr.matches("\\d{4}")) {
                System.out.println("        Validation failed: Year must be exactly 4 digits.");
                continue;
            }
            try {
                year = Integer.parseInt(yearStr);
                break;
            } catch (NumberFormatException e) {
                System.out.println("        Validation failed: Invalid year format.");
            }
        }

        boolean success = libraryService.updateBook(isbn, title, author, year);
        if (success) {
            System.out.println("        --> Book successfully updated.");
        } else {
            System.out.println("        --> Book update failed or no changes made.");
        }
    }

    private static void deleteBookInteractive(Scanner scanner) {
        System.out.println("\n        --- Delete Book ---");
        String isbn;
        while (true) {
            System.out.print("        Enter ISBN of the book to delete (Format: ISBN-XXX, or 'q' to cancel): ");
            isbn = scanner.nextLine().trim();
            if (isbn.equalsIgnoreCase("q")) return;
            if (isbn.isEmpty()) {
                System.out.println("        Validation failed: ISBN cannot be empty.");
                continue;
            }
            if (!isbn.matches("^ISBN-[A-Za-z0-9]+$")) {
                System.out.println("        Validation failed: ISBN must be in the format 'ISBN-XXX' (e.g. ISBN-001).");
                continue;
            }
            if (!libraryService.isBookExists(isbn)) {
                System.out.println("        --> Book with ISBN '" + isbn + "' does not exist. Try again.");
                continue;
            }
            break;
        }
        
        boolean success = libraryService.deleteBook(isbn);
        if (success) {
            System.out.println("        --> Book successfully deleted.");
        } else {
            System.out.println("        --> Delete failed. Book may not exist or is currently borrowed.");
        }
    }

    private static void updateMemberInteractive(Scanner scanner) {
        System.out.println("\n        --- Update Member ---");
        int memberId;
        while (true) {
            System.out.print("        Enter Member ID to update (or 0 to cancel): ");
            String idStr = scanner.nextLine().trim();
            if (idStr.equals("0")) return;
            try {
                memberId = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                System.out.println("        Invalid input. Member ID must be a number.");
                continue;
            }
            if (!libraryService.isValidMember(memberId)) {
                System.out.println("        --> Member ID '" + memberId + "' does not exist. Try again.");
                continue;
            }
            break;
        }

        String name;
        while (true) {
            System.out.print("        Enter new Name: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("        Name cannot be empty. Try again.");
                continue;
            }
            break;
        }

        boolean success = libraryService.updateMember(memberId, name);
        if (success) {
            System.out.println("        --> Member successfully updated.");
        } else {
            System.out.println("        --> Member update failed. Member ID may not exist.");
        }
    }

    private static void deleteMemberInteractive(Scanner scanner) {
        System.out.println("\n        --- Delete Member ---");
        int memberId;
        while (true) {
            System.out.print("        Enter Member ID to delete (or 0 to cancel): ");
            String idStr = scanner.nextLine().trim();
            if (idStr.equals("0")) return;
            try {
                memberId = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                System.out.println("        Invalid input. Member ID must be a number.");
                continue;
            }
            if (!libraryService.isValidMember(memberId)) {
                System.out.println("        --> Member ID '" + memberId + "' does not exist. Try again.");
                continue;
            }
            break;
        }

        boolean success = libraryService.deleteMember(memberId);
        if (success) {
            System.out.println("        --> Member successfully deleted.");
        } else {
            System.out.println("        --> Delete failed. Member may not exist or has unreturned books.");
        }
    }
}
