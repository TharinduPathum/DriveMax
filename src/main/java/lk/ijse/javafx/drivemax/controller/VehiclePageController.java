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
import lk.ijse.javafx.drivemax.bo.custom.VehicleBO;
import lk.ijse.javafx.drivemax.dto.VehicleDto;
import lk.ijse.javafx.drivemax.dto.tm.VehicleTM;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class VehiclePageController implements Initializable {

    public AnchorPane ancPane;
    public Label vehidValueLabel;
    public TextField cusIdField;
    public TextField brandField;
    public TextField regNoField;
    public TextField typeField;

    private final VehicleBO vehicleBO = BOFactory.getInstance().getBO(BOTypes.VEHICLE);

    public TableView<VehicleTM> vehicleTable;
    public TableColumn<VehicleTM, String> colVehicleId;
    public TableColumn<VehicleTM, String> colCustomerId;
    public TableColumn<VehicleTM, String> colType;
    public TableColumn<VehicleTM, String> colBrand;
    public TableColumn<VehicleTM, String> colRegNo;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colRegNo.setCellValueFactory(new PropertyValueFactory<>("regNo"));

        // C001
//        lblId.setText("C001");
        try {
            loadNextId();
            loadTableData();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(
                    Alert.AlertType.ERROR, "Fail to load data..!"
            ).show();
        }

        vehicleTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                vehidValueLabel.setText(newSelection.getVehId());
                cusIdField.setText(newSelection.getCusId());
                typeField.setText(newSelection.getType());
                brandField.setText(newSelection.getBrand());
                regNoField.setText(newSelection.getRegNo());
            }
        });
    }

    public void loadTableData() throws SQLException {
        // 1. Long code
        ArrayList<VehicleDto> vehicleDTOArrayList = vehicleBO.getAllVehicle();
        Collections.reverse(vehicleDTOArrayList);
        ObservableList<VehicleTM> list = FXCollections.observableArrayList();

        for (VehicleDto vehicleDto : vehicleDTOArrayList){
            VehicleTM vehicleTM = new VehicleTM(
                    vehicleDto.getVehicleId(),
                    vehicleDto.getCustomerId(),
                    vehicleDto.getType(),
                    vehicleDto.getBrand(),
                    vehicleDto.getRegNo()
            );
            list.add(vehicleTM);
        }
        vehicleTable.setItems(list);
    }

    private void loadNextId() throws SQLException {
        String nextId = vehicleBO.getNextId();
        vehidValueLabel.setText(nextId);
    }

    private boolean isValid() {
        String customerId = cusIdField.getText();
        String type = typeField.getText();
        String brand = brandField.getText();
        String regNo = regNoField.getText();

        if (customerId == null || !customerId.matches("C\\d{3}")) {
            new Alert(Alert.AlertType.ERROR, "Customer ID must be in the format 'C000' (e.g., C001)").show();
            return false;
        }

        if (type == null || !type.matches("[A-Za-z ]+")) {
            new Alert(Alert.AlertType.ERROR, "Vehicle type must contain only letters and spaces").show();
            return false;
        }

        if (brand == null || !brand.matches("[A-Za-z ]+")) {
            new Alert(Alert.AlertType.ERROR, "Brand must contain only letters and spaces").show();
            return false;
        }

        if (regNo == null || !regNo.matches("[A-Z0-9-]+")) {
            new Alert(Alert.AlertType.ERROR, "Registration number must contain only capital letters, digits, and dashes").show();
            return false;
        }

        return true;
    }


    public void BtnSaveOnAction(ActionEvent event) {

        if (!isValid()) return;

        String vehicleId = vehidValueLabel.getText();
        String customerId = cusIdField.getText();
        String type = typeField.getText();
        String brand = brandField.getText();
        String regNo = regNoField.getText();

        VehicleDto vehicleDto = new VehicleDto(
                vehicleId,
                customerId,
                type,
                brand,
                regNo
        );

        try {
            boolean isSave = vehicleBO.saveVehicle(vehicleDto);
            if (isSave) {
                loadNextId();
                loadTableData();

                cusIdField.setText("");
                typeField.setText("");
                brandField.setText("");
                regNoField.setText("");

                new Alert(
                        Alert.AlertType.INFORMATION, "Vehicle saved successfully..!"
                ).show();
            } else {
                new Alert(
                        Alert.AlertType.ERROR, "Fail to save vehicle..!"
                ).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(
                    Alert.AlertType.ERROR, "Fail to save vehicle..!"
            ).show();
        }
//       Static method call using classname CustomerModel.saveCustomer();


    }


    public void btnDeleteOnAction(ActionEvent event) {
        String vehicleId = vehidValueLabel.getText();

        try {
            boolean isDeleted = vehicleBO.deleteVehicle(vehicleId);
            if (isDeleted) {
                loadNextId();
                loadTableData();
                btnResetOnAction(null); // Reset fields

                new Alert(Alert.AlertType.INFORMATION, "Vehicle deleted successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete vehicle.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while deleting vehicle.").show();
        }
    }



    public void btnRecordOnAction(ActionEvent event) {
       navigateTo("/view/RecordPage.fxml");
    }

    public void btnResetOnAction(ActionEvent event) {
        try {
            loadNextId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cusIdField.setText("");
        typeField.setText("");
        brandField.setText("");
        regNoField.setText("");
        vehicleTable.getSelectionModel().clearSelection();
    }


    public void btnUpdateOnAction(ActionEvent event) {

        if (!isValid()) return;

        String vehicleId = vehidValueLabel.getText();
        String customerId = cusIdField.getText();
        String type = typeField.getText();
        String brand = brandField.getText();
        String regNo = regNoField.getText();

        VehicleDto vehicleDto = new VehicleDto(
                vehicleId,
                customerId,
                type,
                brand,
                regNo
        );

        try {
            boolean isUpdated = vehicleBO.updateVehicle(vehicleDto);
            if (isUpdated) {
                loadTableData();
                new Alert(Alert.AlertType.INFORMATION, "Vehicle updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update vehicle.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while updating vehicle.").show();
        }
    }

    @FXML
    void btnRepairOnAction(ActionEvent event) {
       navigateTo("/view/RepairPage.fxml");
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
