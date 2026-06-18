package lms.concurrent;

import lms.service.LibraryService;

public class BorrowTask implements Runnable {
    private final LibraryService libraryService;
    private final int librarianId;
    private final int memberId;
    private final String isbn;

    public BorrowTask(LibraryService libraryService, int librarianId, int memberId, String isbn) {
        this.libraryService = libraryService;
        this.librarianId = librarianId;
        this.memberId = memberId;
        this.isbn = isbn;
    }

    @Override
    public void run() {
        System.out.println("Librarian " + librarianId + " is processing borrowing request for Member " + memberId + " and Book " + isbn);
        
        boolean success = libraryService.borrowBook(memberId, isbn);
        
        if (success) {
            System.out.println("--> Librarian " + librarianId + " SUCCESS: Borrowed book " + isbn + " for member " + memberId);
        } else {
            System.out.println("--> Librarian " + librarianId + " FAILED: Could not borrow book " + isbn + " for member " + memberId);
        }
    }
}
