package ui.view_checkout;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.checkout.CheckoutController;

/**
 * @author kojusujan1111@gmail.com 02/12/21
 */

public class ViewCheckoutApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ViewCheckoutController.class.getResource("view_checkout.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        primaryStage.setTitle("Checkout");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
