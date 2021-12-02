package ui;

import business.Book;
import business.BookCopy;
import business.HelperUtils;
import business.LibraryMember;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.ArrayList;

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

    List<BookCopy> bookCopyList;
    LibraryMember libraryMember;

    public CheckoutController() {
        bookCopyList = new ArrayList<>();
    }

    public void addBook() {
        if (HelperUtils.isBlankOrNull(memberIdTextBox.getText())) {
            showErrorAlert("Member Id empty");
            return;
        }
        if (!memberIdPresent(memberIdTextBox.getText())) {
            showErrorAlert("Member Id not found");
            return;
        }
        if (HelperUtils.isBlankOrNull(isbnTextBox.getText())) {
            showErrorAlert("ISBN empty");
            return;
        }
        if (!isbnPresent(isbnTextBox.getText())) {
            showErrorAlert("ISBN not found");
            return;
        }

        Book book = getBookCopyWithIsbn(isbnTextBox.getText());
        assert book != null;
        // BookCopy bookCopy = book.getNextAvailableCopy();
        this.libraryMember = getMemberWithId(memberIdTextBox.getText());

        //memberIdTextBox.setText("");
        isbnTextBox.setText("");
        copyNumberColumn.setCellValueFactory(new PropertyValueFactory("copyNum"));
        isbnColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getBook().getIsbn()));
        titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getBook().getTitle()));
        // selectedBookTable.setItems(addBookToSelectedBookTable(bookCopy));
        showInfoAlert("Book Added Success");
        memberIdTextBox.setEditable(false);
    }

    private BookCopy getAvailableCopyOfTheBook(Book book) {
        Book b = new Book("sad", "jaava", 7, new ArrayList<>());

        return b.getCopies()[0];
    }

    private LibraryMember getMemberWithId(String id) {
        return null;
    }

    private Book getBookCopyWithIsbn(String isbn) {
        return null;
    }

    public void checkOut() {
        showInfoAlert("Checkout Success");
    }


    private ObservableList<BookCopy> addBookToSelectedBookTable(BookCopy bookCopy) {
        Book book = new Book("sad", "jaava", 7, new ArrayList<>());
        this.bookCopyList.add(book.getCopies()[0]);
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
        this.memberIdTextBox.setText("");
        this.isbnTextBox.setText("");
        this.bookCopyList = new ArrayList<>();
        this.memberIdTextBox.setEditable(true);
    }
}
