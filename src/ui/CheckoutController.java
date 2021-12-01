package ui;

import business.Book;
import business.BookCopy;
import business.HelperUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

/**
 * @author kojusujan1111@gmail.com 01/12/21
 */

public class CheckoutController {

    @FXML
    public TextField member_id_text_box;

    @FXML
    public TextField isbn_text_box;

    @FXML
    public Button add_button;

    @FXML
    public TableView<BookCopy> selectedBookTable;

    @FXML
    public TableColumn<BookCopy, SimpleStringProperty> isbnColumn;

    @FXML
    public TableColumn<BookCopy, SimpleStringProperty> nameColumn;

    @FXML
    public TableColumn<BookCopy, Integer> copyNumberColumn;

    @FXML
    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public void addBook(ActionEvent addActionEvent) {
        if (HelperUtils.isBlankOrNull(member_id_text_box.getText())) {
            showAlert(alert, "Member Id empty");
            return;
        }
        if (!memberIdPresent(member_id_text_box.getText())) {
            showAlert(alert, "Member Id not found");
            return;
        }
        if (HelperUtils.isBlankOrNull(isbn_text_box.getText())) {
            showAlert(alert, "ISBN empty");
            return;
        }
        if (!isbnPresent(isbn_text_box.getText())) {
            showAlert(alert, "ISBN not found");
            return;
        }

        showAlert(alert, "Book Added Success");
        member_id_text_box.setText("");
        isbn_text_box.setText("");
        selectedBookTable.setItems(getBooks());
    }

    private ObservableList<BookCopy> getBooks() {
        Book java = new Book("a23-13", "java", 7, new ArrayList<>());
        java.addCopy();
        ObservableList<BookCopy> bookCopies = FXCollections.observableArrayList();
        BookCopy[] copies = java.getCopies();
        bookCopies.add(copies[0]);
        return bookCopies;
    }


    private void showAlert(Alert alert, String errorMessage) {
        alert.setHeaderText(errorMessage);
        alert.setTitle("Error");
        alert.show();
    }

    private boolean memberIdPresent(String memberId) {
        return true;
    }

    private boolean isbnPresent(String isbn) {
        return true;
    }

}
