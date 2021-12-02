package business;

import java.time.LocalDate;

/**
 * @author kojusujan1111@gmail.com 01/12/21
 */

public class Checkout {
    private String id;
    private LibraryMember member;
    private BookCopy bookCopy;
    private LocalDate checkoutDate;
    private LocalDate dueDate;

    public Checkout(String id, LibraryMember member, BookCopy bookCopy, LocalDate checkoutDate, LocalDate dueDate) {
        this.id = id;
        this.member = member;
        this.bookCopy = bookCopy;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
    }

    public String getId() {
        return id;
    }

    public LibraryMember getMember() {
        return member;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}
