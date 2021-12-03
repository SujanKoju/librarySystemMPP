package dataaccess;

import java.util.HashMap;
import java.util.Optional;

import business.Book;
import business.Checkout;
import business.LibraryMember;

public interface DataAccess {
    HashMap<String, Book> readBooksMap();
    Optional<Book> getBookWithGivenIsbn(String isbn);


    HashMap<String, User> readUserMap();

    HashMap<String, LibraryMember> readMemberMap();

    HashMap<String, Checkout> readCheckOutMap();

    void saveBook(Book book);

    void saveNewMember(LibraryMember member);

    void saveCheckOut(Checkout checkout);
}
