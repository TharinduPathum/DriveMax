package lk.ijse.javafx.drivemax.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public AnchorPane ancPane;
    public Button homeButton;
    public Button employeeButton;
    public Button vehicleButton;
    public Button inventoryButton;
    public Button customerButton;


    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        navigateTo("/view/HomePage.fxml");
    }

    @FXML
    void btnCustomer(ActionEvent event) throws IOException {
        highlightButton(customerButton);
         navigateTo("/view/CustomerPage.fxml");

    }

    @FXML
    void btnEmployee(ActionEvent event) {
        highlightButton(employeeButton);
        navigateTo("/view/EmployeePage.fxml");
    }

    @FXML
    void btnHome(ActionEvent event) {
        highlightButton(homeButton);
        navigateTo("/view/HomePage.fxml");
    }

    @FXML
    void btnInventory(ActionEvent event) {
        highlightButton(inventoryButton);
        navigateTo("/view/InventoryPage.fxml");
    }

    @FXML
    void btnVehicle(ActionEvent event) {
        highlightButton(vehicleButton);
        navigateTo("/view/VehiclePage.fxml");
    }

    private void resetButtonStyles() {
        String defaultStyle = "-fx-background-color: #b2bec3;";
        homeButton.setStyle(defaultStyle);
        customerButton.setStyle(defaultStyle);
        employeeButton.setStyle(defaultStyle);
        vehicleButton.setStyle(defaultStyle);
        inventoryButton.setStyle(defaultStyle);
    }

    private void highlightButton(Button button) {
        resetButtonStyles();
        button.setStyle("-fx-background-color: #7f8c8d;");
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
    void btnLogOut(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/LoginPage.fxml"));
                    Parent loginRoot = fxmlLoader.load();
                    Scene loginScene = new Scene(loginRoot);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(loginScene);
                    stage.setTitle("Login");
                    stage.centerOnScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Failed to load login screen!").show();
                }
            }
            // If NO is selected, do nothing — stay on current page
        });







    }

}
