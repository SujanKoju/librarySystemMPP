package ui.checkout;

import business.*;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author kojusujan1111@gmail.com 01/12/21
 */

public class CheckoutController {
    @FXML
    public TextField memberIdTextBox;
    @FXML
    public TextField isbnTextBox;
    @FXML
    public Button addButton;
    @FXML
    public Button checkOutButton;
    @FXML
    public TableView<BookCopy> selectedBookTable = new TableView<>();
    @FXML
    public TableColumn<BookCopy, String> isbnColumn = new TableColumn<>();
    @FXML
    public TableColumn<BookCopy, String> titleColumn = new TableColumn<>();
    @FXML
    public TableColumn<BookCopy, Integer> copyNumberColumn = new TableColumn<>();
    @FXML
    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    @FXML
    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);

    public DataAccessFacade dataAccessFacade;

    List<BookCopy> bookCopyList;
    LibraryMember libraryMember;

    public CheckoutController() {
        bookCopyList = new ArrayList<>();
        dataAccessFacade = new DataAccessFacade();
    }

    public void addBook() {
        if (validationError()) return;

        Optional<BookCopy> bookCopy = getAvailableBookCopyWithIsbn(isbnTextBox.getText());
        if (!bookCopy.isPresent()) {
            showErrorAlert("Book Not found or all copies are unavailable");
            return;
        }

        Optional<LibraryMember> memberWithId = getMemberWithId(memberIdTextBox.getText());
        if (!memberWithId.isPresent()) {
            showErrorAlert("Member with given id not found.");
            return;
        }

        this.libraryMember = memberWithId.get();
        isbnTextBox.setText("");
        copyNumberColumn.setCellValueFactory(new PropertyValueFactory("copyNum"));
        isbnColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getBook().getIsbn()));
        titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getBook().getTitle()));
        selectedBookTable.setItems(addBookToSelectedBookTable(bookCopy.get()));
        showInfoAlert("Book Added Success");
        memberIdTextBox.setEditable(false);
    }

    private boolean validationError() {
        if (HelperUtils.isBlankOrNull(memberIdTextBox.getText())) {
            showErrorAlert("Member Id empty");
            return true;
        }
        if (!memberIdPresent(memberIdTextBox.getText())) {
            showErrorAlert("Member Id not found");
            return true;
        }
        if (HelperUtils.isBlankOrNull(isbnTextBox.getText())) {
            showErrorAlert("ISBN empty");
            return true;
        }
        if (!isbnPresent(isbnTextBox.getText())) {
            showErrorAlert("ISBN not found");
            return true;
        }
        return false;
    }


    private Optional<LibraryMember> getMemberWithId(String id) {
        HashMap<String, LibraryMember> memberHashMap = dataAccessFacade.readMemberMap();
        if (!memberHashMap.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.ofNullable(memberHashMap.get(id));
    }

    private Optional<BookCopy> getAvailableBookCopyWithIsbn(String isbn) {
        HashMap<String, Book> bookHashMap = dataAccessFacade.readBooksMap();
        if (!bookHashMap.containsKey(isbn)) return Optional.empty();
        Book book = bookHashMap.get(isbn);
        BookCopy nextAvailableCopy = book.getNextAvailableCopy();
        if (nextAvailableCopy == null) return Optional.empty();
        return Optional.of(nextAvailableCopy);
    }

    public void checkOut() {
        DataAccess dataAccess = new DataAccessFacade();
        LibraryMember libraryMember = this.libraryMember;
        List<BookCopy> bookCopyList = this.bookCopyList;
        bookCopyList.forEach(bookCopy -> {
            String code = HelperUtils.generateUUID();
            dataAccess.saveCheckOut(prepareCheckOutEntity(libraryMember, bookCopy, code));
            updateBookCopyToUnavailable(dataAccess, bookCopy);
        });
        showInfoAlert("Checkout Success");
        resetForm();

    }

    private void updateBookCopyToUnavailable(DataAccess dataAccess, BookCopy bookCopy) {
        Book book = bookCopy.getBook();
        for (BookCopy copy : book.getCopies()) {
            if (copy.getCopyNum() == bookCopy.getCopyNum()) {
                copy.changeAvailability();
            }
        }
        dataAccess.saveBook(book);
    }

    private Checkout prepareCheckOutEntity(LibraryMember libraryMember, BookCopy bookCopy, String code) {
        Checkout checkout = new Checkout();
        checkout.setId(HelperUtils.generateUUID());
        checkout.setCode(code);
        checkout.setBookCopy(bookCopy);
        checkout.setMember(libraryMember);
        checkout.setCheckoutDate(LocalDate.now());
        checkout.setDueDate(LocalDate.now().plusDays(bookCopy.getBook().getMaxCheckoutLength()));
        return checkout;
    }


    private ObservableList<BookCopy> addBookToSelectedBookTable(BookCopy bookCopy) {
        this.bookCopyList.add(bookCopy);
        return FXCollections.observableArrayList(this.bookCopyList);
    }


    private void showErrorAlert(String errorMessage) {
        errorAlert.setHeaderText(errorMessage);
        errorAlert.setTitle("Error");
        errorAlert.show();
    }

    private void showInfoAlert(String errorMessage) {
        infoAlert.setHeaderText(errorMessage);
        infoAlert.show();
    }

    private boolean memberIdPresent(String memberId) {
        return true;
    }

    private boolean isbnPresent(String isbn) {
        return true;
    }

    public void cancel() {
        resetForm();
    }

    private void resetForm() {
        this.memberIdTextBox.setText("");
        this.isbnTextBox.setText("");
        this.bookCopyList = new ArrayList<>();
        this.libraryMember = null;
        this.memberIdTextBox.setEditable(true);
        selectedBookTable.getItems().clear();
    }
}
