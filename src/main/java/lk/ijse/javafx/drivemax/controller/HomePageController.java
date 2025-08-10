package lk.ijse.javafx.drivemax.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    public Button customerButton;
    public Button employeeButton;
    public Button inventoryButton;
    public Button vehicleButton;

    @FXML
    private AnchorPane ancPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void navigateTo(String path) {
        try {
            ancPane.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancPane.widthProperty());
            anchorPane.prefHeightProperty().bind(ancPane.heightProperty());

            ancPane.getChildren().add(anchorPane);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnCustomer(ActionEvent event) {
          navigateTo("/view/CustomerPage.fxml");
    }

    @FXML
    void btnEmployee(ActionEvent event) {
         navigateTo("/view/EmployeePage.fxml");
    }

    @FXML
    void btnInventory(ActionEvent event) {
         navigateTo("/view/InventoryPage.fxml");
    }

    @FXML
    void btnVehicle(ActionEvent event) {
         navigateTo("/view/VehiclePage.fxml");
    }


}
