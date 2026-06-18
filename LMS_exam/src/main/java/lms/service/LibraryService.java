package lms.service;

import lms.dao.DatabaseConnection;
import lms.models.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {

    // Retrieve all available books from DB
    public List<Book> listAvailableBooks() {
        List<Book> availableBooks = new ArrayList<>();
        String query = "SELECT * FROM books WHERE is_available = 1";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                availableBooks.add(new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("publication_year"),
                        rs.getBoolean("is_available")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error listing available books: " + e.getMessage());
        }

        return availableBooks;
    }

    // Borrow a book (synchronized to prevent two librarians from issuing the same book simultaneously)
    public synchronized boolean borrowBook(int memberId, String isbn) {
        String countQuery = "SELECT COUNT(*) AS borrowed_count FROM borrowing_records WHERE member_id = ? AND return_date IS NULL";
        String updateBookQuery = "UPDATE books SET is_available = 0 WHERE isbn = ? AND is_available = 1";
        String insertRecordQuery = "INSERT INTO borrowing_records (member_id, isbn, borrow_date) VALUES (?, ?, date('now'))";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);

            // 1. Check if member has 5 or more books
            try (PreparedStatement pstmt = conn.prepareStatement(countQuery)) {
                pstmt.setInt(1, memberId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next() && rs.getInt("borrowed_count") >= 5) {
                        System.out.println("Member " + memberId + " cannot borrow more than 5 books.");
                        return false;
                    }
                }
            }

            // 2. Mark book as unavailable
            try (PreparedStatement pstmt = conn.prepareStatement(updateBookQuery)) {
                pstmt.setString(1, isbn);
                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated == 0) {
                    System.out.println("Book " + isbn + " is not available for borrowing.");
                    conn.rollback();
                    return false;
                }
            }

            // 3. Record the transaction
            try (PreparedStatement pstmt = conn.prepareStatement(insertRecordQuery)) {
                pstmt.setInt(1, memberId);
                pstmt.setString(2, isbn);
                pstmt.executeUpdate();
            }

            // Commit transaction
            conn.commit();
            // System.out.println("Member " + memberId + " successfully borrowed book " + isbn);
            return true;

        } catch (SQLException e) {
            System.err.println("Error during borrowing transaction: " + e.getMessage());
            return false;
        }
    }

    // Return a book
    public boolean returnBook(int memberId, String isbn) {
        String updateRecordQuery = "UPDATE borrowing_records SET return_date = date('now') WHERE member_id = ? AND isbn = ? AND return_date IS NULL";
        String updateBookQuery = "UPDATE books SET is_available = 1 WHERE isbn = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            int recordsUpdated = 0;
            // 1. Mark record as returned
            try (PreparedStatement pstmt = conn.prepareStatement(updateRecordQuery)) {
                pstmt.setInt(1, memberId);
                pstmt.setString(2, isbn);
                recordsUpdated = pstmt.executeUpdate();
            }

            if (recordsUpdated == 0) {
                System.out.println("No active borrowing record found for Member " + memberId + " and Book " + isbn);
                conn.rollback();
                return false;
            }

            // 2. Mark book as available
            try (PreparedStatement pstmt = conn.prepareStatement(updateBookQuery)) {
                pstmt.setString(1, isbn);
                pstmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Member " + memberId + " successfully returned book " + isbn);
            return true;

        } catch (SQLException e) {
            System.err.println("Error during returning transaction: " + e.getMessage());
            return false;
        }
    }
}
