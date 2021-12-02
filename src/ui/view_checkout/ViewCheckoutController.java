package ui.view_checkout;

import business.Checkout;
import dataaccess.DataAccessFacade;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author kojusujan1111@gmail.com 02/12/21
 */

public class ViewCheckoutController {

    @FXML
    public TextField memberIdTextBox;
    @FXML
    public Button searchButton;
    @FXML
    public Button printButton;
    @FXML
    public TableView<Checkout> checkoutTable = new TableView<>();
    @FXML
    public TableColumn<Checkout, String> memberNameColumn = new TableColumn<>();
    @FXML
    public TableColumn<Checkout, String> bookTitleColumn = new TableColumn<>();
    @FXML
    public TableColumn<Checkout, Integer> copyNumberColumn = new TableColumn<>();
    @FXML
    public TableColumn<Checkout, LocalDate> checkOutDateColumn = new TableColumn<>();
    @FXML
    public TableColumn<Checkout, LocalDate> dueDateColumn = new TableColumn<>();
    List<Checkout> checkoutList;

    public ViewCheckoutController() {
        this.checkoutList = new ArrayList<>();
    }


    public void search() {
        String memberId = this.memberIdTextBox.getText();
        memberNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getMember().getFirstName()));
        bookTitleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getBookCopy().getBook().getTitle()));
        checkOutDateColumn.setCellValueFactory(new PropertyValueFactory<>("checkoutDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        checkoutTable.setItems(getCheckOutOfTheMember(memberId));
    }

    private ObservableList<Checkout> getCheckOutOfTheMember(String memberId) {
        this.checkoutList.clear();
        DataAccessFacade dataAccessFacade = new DataAccessFacade();
        HashMap<String, Checkout> checkoutHashMap = dataAccessFacade.readCheckOutMap();
        checkoutHashMap.forEach((s, checkout) -> {
            if (checkout.getMember().getMemberId().equalsIgnoreCase(memberId)) {
                this.checkoutList.add(checkout);
            }
        });
        return FXCollections.observableArrayList(this.checkoutList);
    }

    public void print() {
        if (this.checkoutList.size() != 0) {
            System.out.println("------ CHECKOUTS -----");
            this.checkoutList.forEach(checkout -> {
                System.out.println("Book : " + checkout.getBookCopy().getBook().getTitle());
                System.out.println("Copy no : " + checkout.getBookCopy().getCopyNum());
                System.out.println("Checkout Date : " + checkout.getCheckoutDate());
                System.out.println("Due date : " + checkout.getDueDate());
                System.out.println("----------------------");
            });

        }

    }
}
