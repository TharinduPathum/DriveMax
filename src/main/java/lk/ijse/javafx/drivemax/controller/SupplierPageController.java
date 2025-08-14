package lk.ijse.javafx.drivemax.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.javafx.drivemax.bo.BOFactory;
import lk.ijse.javafx.drivemax.bo.BOTypes;
import lk.ijse.javafx.drivemax.bo.custom.InvoiceBO;
import lk.ijse.javafx.drivemax.bo.custom.SupplierBO;
import lk.ijse.javafx.drivemax.dto.SupplierDto;
import lk.ijse.javafx.drivemax.dto.tm.SupplierTM;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class SupplierPageController implements Initializable {

    public TextField addressField;
    public TextField emailField;
    public TextField nameField;
    public TextField phoneNoField;
    public Label supidValueLabel;

    private final SupplierBO supplierBO = BOFactory.getInstance().getBO(BOTypes.SUPPLIER);

    public TableView<SupplierTM> supplierTable;
    public TableColumn<SupplierTM, String> colSupId;
    public TableColumn<SupplierTM, String> colName;
    public TableColumn<SupplierTM, String> colAddress;
    public TableColumn<SupplierTM, String> colEmail;
    public TableColumn<SupplierTM, String> colPhoneNo;

    public AnchorPane ancPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSupId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhoneNo.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // C001
//        lblId.setText("C001");
        try {
            loadNextId();
            loadTableData();
        } catch (Exception e) {
            new Alert(
                    Alert.AlertType.ERROR, "Fail to load data..!"
            ).show();
        }

        supplierTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                supidValueLabel.setText(newSelection.getId());
                nameField.setText(newSelection.getName());
                addressField.setText(newSelection.getAddress());
                emailField.setText(newSelection.getEmail());
                phoneNoField.setText(newSelection.getPhone());
            }
        });

    }

    private void loadTableData() throws SQLException {
        // 1. Long code
        ArrayList<SupplierDto> supplierDTOArrayList = supplierBO.getAllSupplier();
        Collections.reverse(supplierDTOArrayList);
        ObservableList<SupplierTM> list = FXCollections.observableArrayList();

        for (SupplierDto supplierDto : supplierDTOArrayList){
            SupplierTM supplierTM = new SupplierTM(
                    supplierDto.getSupplierId(),
                    supplierDto.getName(),
                    supplierDto.getAddress(),
                    supplierDto.getEmail(),
                    supplierDto.getPhone()
            );
            list.add(supplierTM);
        }
        supplierTable.setItems(list);
    }

    private void loadNextId() throws SQLException {
        String nextId = supplierBO.getNextId();
        supidValueLabel.setText(nextId);
    }

    private boolean isValidInput() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneNoField.getText().trim();

        if (!name.matches("[a-zA-Z ]+")) {
            showAlert("Invalid name. Only letters and spaces are allowed.");
            return false;
        }

        if (!address.matches("[a-zA-Z ]+")) {
            showAlert("Invalid address. Only letters and spaces are allowed.");
            return false;
        }

        if (!email.matches("^[\\w.-]+@(gmail\\.com|yahoo\\.com)$")) {
            showAlert("Invalid email. Must end with @gmail.com or @yahoo.com.");
            return false;
        }

        if (!phone.matches("\\d{10}")) {
            showAlert("Invalid phone number. Must be exactly 10 digits.");
            return false;
        }

        return true;
    }

    private void showAlert(String message) {
        new Alert(Alert.AlertType.WARNING, message).show();
    }


    public void BtnSaveOnAction(ActionEvent event) {

        if (!isValidInput()) return;

        String supplierId = supidValueLabel.getText();
        String name = nameField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String phone = phoneNoField.getText();

        SupplierDto supplierDto = new SupplierDto(
                supplierId,
                name,
                address,
                email,
                phone
        );

        try {
            boolean isSave = supplierBO.saveSupplier(supplierDto);
            if (isSave) {
                loadNextId();
                loadTableData();

                nameField.setText("");
                addressField.setText("");
                emailField.setText("");
                phoneNoField.setText("");

                new Alert(
                        Alert.AlertType.INFORMATION, "Supplier saved successfully..!"
                ).show();
            } else {
                new Alert(
                        Alert.AlertType.ERROR, "Fail to save supplier..!"
                ).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(
                    Alert.AlertType.ERROR, "Fail to save supplier..!"
            ).show();
        }
//       Static method call using classname CustomerModel.saveCustomer();

    }


    public void btnDeleteOnAction(ActionEvent event) {
        String supplierId = supidValueLabel.getText();

        try {
            boolean isDeleted = supplierBO.deleteSupplier(supplierId);
            if (isDeleted) {
                loadNextId();
                loadTableData();
                btnResetOnAction(null); // Clear fields

                new Alert(Alert.AlertType.INFORMATION, "Supplier deleted successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete supplier.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while deleting supplier.").show();
        }
    }

    @FXML
    void btnReportOnAction(ActionEvent event) {

    }

    public void btnResetOnAction(ActionEvent event) {
        try {
            loadNextId();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        nameField.setText("");
        addressField.setText("");
        emailField.setText("");
        phoneNoField.setText("");

        supplierTable.getSelectionModel().clearSelection();
    }


    public void btnUpdateOnAction(ActionEvent event) {

        if (!isValidInput()) return;

        String supplierId = supidValueLabel.getText();
        String name = nameField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String phone = phoneNoField.getText();

        SupplierDto supplierDto = new SupplierDto(
                supplierId,
                name,
                address,
                email,
                phone
        );

        try {
            boolean isUpdated = supplierBO.updateSupplier(supplierDto);
            if (isUpdated) {
                loadTableData();
                new Alert(Alert.AlertType.INFORMATION, "Supplier updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update supplier.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while updating supplier.").show();
        }
    }

    public void btnAnnounceOnAction(ActionEvent actionEvent) {
        navigateTo("/view/EmailPage.fxml");

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


}
