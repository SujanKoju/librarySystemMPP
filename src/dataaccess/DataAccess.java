package dataaccess;

import java.util.HashMap;

import business.Book;
import business.Checkout;
import business.LibraryMember;
import dataaccess.DataAccessFacade.StorageType;

public interface DataAccess {
    public HashMap<String, Book> readBooksMap();

    public HashMap<String, User> readUserMap();

    public HashMap<String, LibraryMember> readMemberMap();

    public HashMap<String, Checkout> readCheckOutMap();

    public void saveBook(Book book);

    public void saveNewMember(LibraryMember member);

    public void saveCheckOut(Checkout checkout);
}
