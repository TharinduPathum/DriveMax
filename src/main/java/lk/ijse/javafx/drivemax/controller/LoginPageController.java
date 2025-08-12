package lk.ijse.javafx.drivemax.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPageController {

    @FXML
    private PasswordField txtGetPassword;

    @FXML
    private TextField txtGetUsername;

    @FXML
    void btnLogin(ActionEvent event) {
        try {
            String username = txtGetUsername.getText().trim();
            String password = txtGetPassword.getText().trim();

            // Dummy check
            if (username.equals("a") && password.equals("a")) {
                Parent root = FXMLLoader.load(getClass().getResource("/view/Dashboards.fxml"));
                Stage stage = (Stage) txtGetUsername.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Dashboard");
                stage.show();
            } else {
                throw new IllegalArgumentException("Invalid username or password.");
            }

        } catch (IllegalArgumentException e) {
            showError("Login Failed", e.getMessage());
        } catch (IOException e) {
            showError("System Error", "Failed to load Dashboard.fxml");
        } catch (Exception e) {
            showError("Unexpected Error", "An unexpected error occurred during login.");
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
