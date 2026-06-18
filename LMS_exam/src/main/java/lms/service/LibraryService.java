package lms.service;

import lms.dao.DatabaseConnection;
import lms.models.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lms.models.Member;

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

    // List borrowed books for a specific member
    public List<Book> listBorrowedBooks(int memberId) {
        List<Book> borrowedBooks = new ArrayList<>();
        String query = "SELECT b.* FROM books b JOIN borrowing_records r ON b.isbn = r.isbn WHERE r.member_id = ? AND r.return_date IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    borrowedBooks.add(new Book(
                            rs.getString("isbn"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getInt("publication_year"),
                            rs.getBoolean("is_available")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error listing borrowed books: " + e.getMessage());
        }

        return borrowedBooks;
    }

    // Register a new book
    public boolean registerBook(Book book) {
        String query = "INSERT INTO books (isbn, title, author, publication_year, is_available) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setInt(4, book.getPublicationYear());
            pstmt.setBoolean(5, book.isAvailable());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error registering book: " + e.getMessage());
            return false;
        }
    }

    // List all members
    public List<Member> listMembers() {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                members.add(new Member(
                        rs.getInt("member_id"),
                        rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error listing members: " + e.getMessage());
        }

        return members;
    }

    // Check if member exists
    public boolean isValidMember(int memberId) {
        String query = "SELECT 1 FROM members WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error validating member: " + e.getMessage());
            return false;
        }
    }

    // Register a new member and return their generated ID (or -1 if failed)
    public int registerMember(String name) {
        String query = "INSERT INTO members (name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, name);
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            return -1;
        } catch (SQLException e) {
            System.err.println("Error registering member: " + e.getMessage());
            return -1;
        }
    }

    public int getMemberIdByName(String name) {
        String query = "SELECT member_id FROM members WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("member_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding member by name: " + e.getMessage());
        }
        return -1;
    }

    public boolean isBookExists(String isbn) {
        String query = "SELECT 1 FROM books WHERE isbn = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error validating book: " + e.getMessage());
            return false;
        }
    }

    public List<String[]> listAllBooksWithBorrower() {
        List<String[]> books = new ArrayList<>();
        String query = "SELECT b.isbn, b.title, b.author, b.publication_year, b.is_available, " +
                       "m.name as borrower_name " +
                       "FROM books b " +
                       "LEFT JOIN borrowing_records r ON b.isbn = r.isbn AND r.return_date IS NULL " +
                       "LEFT JOIN members m ON r.member_id = m.member_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String[] info = new String[6];
                info[0] = rs.getString("isbn");
                info[1] = rs.getString("title");
                info[2] = rs.getString("author");
                info[3] = String.valueOf(rs.getInt("publication_year"));
                info[4] = rs.getBoolean("is_available") ? "Yes" : "No";
                info[5] = rs.getString("borrower_name"); // can be null
                books.add(info);
            }
        } catch (SQLException e) {
            System.err.println("Error listing books: " + e.getMessage());
        }
        return books;
    }

    public boolean updateBook(String isbn, String title, String author, Integer year) {
        StringBuilder query = new StringBuilder("UPDATE books SET ");
        List<Object> params = new ArrayList<>();
        
        if (title != null && !title.trim().isEmpty()) {
            query.append("title = ?, ");
            params.add(title.trim());
        }
        if (author != null && !author.trim().isEmpty()) {
            query.append("author = ?, ");
            params.add(author.trim());
        }
        if (year != null) {
            query.append("publication_year = ?, ");
            params.add(year);
        }
        
        if (params.isEmpty()) {
            return false; // nothing to update
        }
        
        // Remove trailing comma and space
        query.setLength(query.length() - 2);
        query.append(" WHERE isbn = ?");
        params.add(isbn);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
             
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBook(String isbn) {
        String checkQuery = "SELECT 1 FROM borrowing_records WHERE isbn = ? AND return_date IS NULL";
        String deleteQuery = "DELETE FROM books WHERE isbn = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if currently borrowed
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, isbn);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Cannot delete book: It is currently borrowed.");
                        return false;
                    }
                }
            }
            
            // Delete the book
            try (PreparedStatement delStmt = conn.prepareStatement(deleteQuery)) {
                delStmt.setString(1, isbn);
                return delStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMember(int memberId, String name) {
        String query = "UPDATE members SET name = ? WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, memberId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating member: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMember(int memberId) {
        String checkQuery = "SELECT 1 FROM borrowing_records WHERE member_id = ? AND return_date IS NULL";
        String deleteQuery = "DELETE FROM members WHERE member_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if member has borrowed books
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, memberId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Cannot delete member: They have unreturned books.");
                        return false;
                    }
                }
            }
            
            // Delete the member
            try (PreparedStatement delStmt = conn.prepareStatement(deleteQuery)) {
                delStmt.setInt(1, memberId);
                return delStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting member: " + e.getMessage());
            return false;
        }
    }
}
