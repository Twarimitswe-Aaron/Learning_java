import java.util.ArrayList;
import java.util.List;

/*
 * EXAM QUESTION:
 * Create a simple library management system. Design and implement the classes
 * required for this system considering the following requirements:
 * a) Each book has a unique ISBN, title, author(s), and publication year.
 * b) Each library member has a unique ID, name, and can borrow books.
 * c) A member can borrow a maximum of 5 books at a time.
 * d) The library should track available books and borrowed books.
 * e) Implement methods to borrow and return books.
 * f) Implement a method to display books borrowed by a member.
 * g) Implement a method to display available books.
 * h) Ensure encapsulation and appropriate access modifiers.
 * i) Use inheritance and polymorphism where applicable.
 *
 * DESIGN NOTES (explain this in your exam if asked to justify design):
 * - Book: encapsulated fields, validated in constructor.
 * - Member: holds its own borrowed list, enforces the "max 5 books" rule itself
 *   (a member should be responsible for knowing its own borrowing limit).
 * - Person (abstract) -> Member, Librarian : demonstrates inheritance.
 *   Librarian overrides a method to show polymorphism (e.g. access rights).
 * - Library: the "control" class that owns the master list of books and
 *   coordinates borrow/return, checking availability before delegating to Member.
 * - Custom checked exception used for borrowing-limit and availability errors,
 *   because these are *expected* business rule violations the caller must handle.
 */

// ---------- Custom Exceptions ----------
class BookNotAvailableException extends Exception {
    public BookNotAvailableException(String message) {
        super(message);
    }
}

class BorrowLimitExceededException extends Exception {
    public BorrowLimitExceededException(String message) {
        super(message);
    }
}

class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}

// ---------- Book ----------
class Book {
    private final String isbn;
    private String title;
    private String author;
    private int publicationYear;
    private boolean available;

    public Book(String isbn, String title, String author, int publicationYear) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be empty.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty.");
        }
        int currentYear = java.time.Year.now().getValue();
        if (publicationYear < 1450 || publicationYear > currentYear) {
            throw new IllegalArgumentException("Invalid publication year: " + publicationYear);
        }
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.available = true; // new books start as available
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public boolean isAvailable() {
        return available;
    }

    void setAvailable(boolean available) { // package-private: only Library should flip this
        this.available = available;
    }

    @Override
    public String toString() {
        return String.format("[%s] \"%s\" by %s (%d) - %s",
                isbn, title, author, publicationYear, available ? "Available" : "Borrowed");
    }
}

// ---------- Person hierarchy (inheritance + polymorphism) ----------
abstract class Person {
    protected String id;
    protected String name;

    public Person(String id, String name) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Polymorphic method: every type of person describes their role differently
    public abstract String getRole();

    @Override
    public String toString() {
        return getRole() + " [" + id + "] " + name;
    }
}

class Member extends Person {
    private static final int MAX_BOOKS = 5;
    private final List<Book> borrowedBooks;

    public Member(String id, String name) {
        super(id, name);
        this.borrowedBooks = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "Member";
    }

    public List<Book> getBorrowedBooks() {
        return new ArrayList<>(borrowedBooks); // defensive copy
    }

    void addBorrowedBook(Book book) throws BorrowLimitExceededException {
        if (borrowedBooks.size() >= MAX_BOOKS) {
            throw new BorrowLimitExceededException(
                    name + " has already borrowed the maximum of " + MAX_BOOKS + " books.");
        }
        borrowedBooks.add(book);
    }

    void removeBorrowedBook(Book book) {
        borrowedBooks.remove(book);
    }

    public void displayBorrowedBooks() {
        System.out.println(name + "'s borrowed books (" + borrowedBooks.size() + "/" + MAX_BOOKS + "):");
        if (borrowedBooks.isEmpty()) {
            System.out.println("  (none)");
        } else {
            for (Book b : borrowedBooks) {
                System.out.println("  - " + b);
            }
        }
    }
}

// Demonstrates polymorphism: a different kind of Person with different rights
class Librarian extends Person {
    public Librarian(String id, String name) {
        super(id, name);
    }

    @Override
    public String getRole() {
        return "Librarian";
    }

    public void addNewBook(Library library, Book book) {
        library.addBook(book);
        System.out.println(name + " added new book: " + book.getTitle());
    }
}

// ---------- Library (the controller class) ----------
class Library {
    private final List<Book> catalog = new ArrayList<>();

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Cannot add a null book.");
        }
        boolean duplicate = catalog.stream().anyMatch(b -> b.getIsbn().equals(book.getIsbn()));
        if (duplicate) {
            throw new IllegalArgumentException("A book with ISBN " + book.getIsbn() + " already exists.");
        }
        catalog.add(book);
    }

    private Book findByIsbn(String isbn) throws BookNotFoundException {
        for (Book b : catalog) {
            if (b.getIsbn().equals(isbn)) {
                return b;
            }
        }
        throw new BookNotFoundException("No book found with ISBN: " + isbn);
    }

    public void borrowBook(Member member, String isbn)
            throws BookNotFoundException, BookNotAvailableException, BorrowLimitExceededException {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }
        Book book = findByIsbn(isbn);
        if (!book.isAvailable()) {
            throw new BookNotAvailableException("\"" + book.getTitle() + "\" is currently borrowed.");
        }
        // Check the limit BEFORE marking the book unavailable, so state stays consistent
        member.addBorrowedBook(book);
        book.setAvailable(false);
        System.out.println(member.getName() + " borrowed \"" + book.getTitle() + "\"");
    }

    public void returnBook(Member member, String isbn) throws BookNotFoundException {
        Book book = findByIsbn(isbn);
        member.removeBorrowedBook(book);
        book.setAvailable(true);
        System.out.println(member.getName() + " returned \"" + book.getTitle() + "\"");
    }

    public void displayAvailableBooks() {
        System.out.println("Available books:");
        boolean any = false;
        for (Book b : catalog) {
            if (b.isAvailable()) {
                System.out.println("  - " + b);
                any = true;
            }
        }
        if (!any) {
            System.out.println("  (none available)");
        }
    }

    public void displayAllBooks() {
        System.out.println("Full catalog:");
        for (Book b : catalog) {
            System.out.println("  - " + b);
        }
    }
}

// ---------- Demo ----------
public class LibrarySystem {
    public static void main(String[] args) {
        Library library = new Library();
        Librarian librarian = new Librarian("L1", "Mr. Habimana");

        try {
            librarian.addNewBook(library, new Book("978-0-1", "Things Fall Apart", "Chinua Achebe", 1958));
            librarian.addNewBook(library, new Book("978-0-2", "Half of a Yellow Sun", "Chimamanda Ngozi Adichie", 2006));
            librarian.addNewBook(library, new Book("978-0-3", "Clean Code", "Robert Martin", 2008));

            Member member = new Member("M1", "Alice Uwase");

            library.borrowBook(member, "978-0-1");
            library.borrowBook(member, "978-0-2");

            member.displayBorrowedBooks();
            library.displayAvailableBooks();

            library.returnBook(member, "978-0-1");
            library.displayAvailableBooks();

            // Trying to borrow a book that's already out
            library.borrowBook(member, "978-0-3");
            library.borrowBook(new Member("M2", "Bob"), "978-0-3"); // will fail - not available

        } catch (BookNotFoundException | BookNotAvailableException | BorrowLimitExceededException e) {
            System.out.println("Library error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
