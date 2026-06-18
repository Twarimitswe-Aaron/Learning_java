package lms.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:library.db";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create books table
            String createBooksTable = "CREATE TABLE IF NOT EXISTS books (" +
                    "isbn TEXT PRIMARY KEY," +
                    "title TEXT NOT NULL," +
                    "author TEXT NOT NULL," +
                    "publication_year INTEGER," +
                    "is_available BOOLEAN DEFAULT 1" +
                    ");";
            stmt.execute(createBooksTable);

            // Create members table
            String createMembersTable = "CREATE TABLE IF NOT EXISTS members (" +
                    "member_id INTEGER PRIMARY KEY," +
                    "name TEXT NOT NULL" +
                    ");";
            stmt.execute(createMembersTable);

            // Create borrowing_records table
            String createRecordsTable = "CREATE TABLE IF NOT EXISTS borrowing_records (" +
                    "record_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "member_id INTEGER," +
                    "isbn TEXT," +
                    "borrow_date DATE," +
                    "return_date DATE," +
                    "FOREIGN KEY (member_id) REFERENCES members(member_id)," +
                    "FOREIGN KEY (isbn) REFERENCES books(isbn)" +
                    ");";
            stmt.execute(createRecordsTable);

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}
