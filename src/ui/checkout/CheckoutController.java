package ui.checkout;

import business.*;
import dataaccess.DataAccessFacade;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ui.view_checkout.ViewCheckoutController;

import java.io.IOException;
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
    public Button viewCheckoutButton;
    @FXML
    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    @FXML
    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);

    public DataAccessFacade dataAccess = new DataAccessFacade();

    List<BookCopy> bookCopyList;
    LibraryMember libraryMember;

    public CheckoutController() {
        bookCopyList = new ArrayList<>();
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
        HashMap<String, LibraryMember> memberHashMap = dataAccess.readMemberMap();
        if (!memberHashMap.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.ofNullable(memberHashMap.get(id));
    }

    private Optional<BookCopy> getAvailableBookCopyWithIsbn(String isbn) {
        HashMap<String, Book> bookHashMap = dataAccess.readBooksMap();
        if (!bookHashMap.containsKey(isbn)) return Optional.empty();
        Book book = bookHashMap.get(isbn);
        BookCopy nextAvailableCopy = book.getNextAvailableCopy(prepareAlreadyAddedBookCopyOfIsbn(isbn, bookCopyList));
        if (nextAvailableCopy == null) return Optional.empty();
        return Optional.of(nextAvailableCopy);
    }

    private HashMap<Integer, Boolean> prepareAlreadyAddedBookCopyOfIsbn(String isbn, List<BookCopy> bookCopyList) {
        HashMap<Integer, Boolean> map = new HashMap<>();
        bookCopyList.forEach(bookCopy -> {
            if (bookCopy.getBook().getIsbn().equalsIgnoreCase(isbn)) map.put(bookCopy.getCopyNum(), true);
        });
        return map;
    }

    public void checkOut() {
        if (bookCopyList.size() > 0) {
            LibraryMember libraryMember = this.libraryMember;
            List<BookCopy> bookCopyList = this.bookCopyList;
            bookCopyList.forEach(bookCopy -> {
                String code = HelperUtils.generateUUID();
                this.dataAccess.saveCheckOut(prepareCheckOutEntity(libraryMember, bookCopy, code));
                updateBookCopyToUnavailable(bookCopy);
            });
            showInfoAlert("Checkout Success");
            resetForm();
        }
    }

    private void updateBookCopyToUnavailable(BookCopy bookCopy) {
        Book book = dataAccess.getBookWithGivenIsbn(bookCopy.getBook().getIsbn()).get();
        for (BookCopy copy : book.getCopies()) {
            if (copy.getCopyNum() == bookCopy.getCopyNum()) {
                copy.changeAvailability();
            }
        }
        this.dataAccess.saveBook(book);
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

    public void viewCheckouts() throws IOException {
        Stage stage = (Stage) viewCheckoutButton.getScene().getWindow();
        stage.close();
        Stage viewCheckoutStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(ViewCheckoutController.class.getResource("view_checkout.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        viewCheckoutStage.setTitle("View Checkout");
        viewCheckoutStage.setScene(scene);
        viewCheckoutStage.show();
    }
}
