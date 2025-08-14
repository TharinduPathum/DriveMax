package lk.ijse.javafx.drivemax.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.javafx.drivemax.bo.BOFactory;
import lk.ijse.javafx.drivemax.bo.BOTypes;
import lk.ijse.javafx.drivemax.bo.custom.InvoiceBO;
import lk.ijse.javafx.drivemax.bo.custom.RecordBO;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.dto.RecordDto;
import lk.ijse.javafx.drivemax.dto.tm.RecordTM;


import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class RecordPageController implements Initializable {

    public TableView<RecordTM> recordTable;
    public TableColumn<RecordTM, String> colRecord;
    public TableColumn<RecordTM, String> colVehicleId;
    public TableColumn<RecordTM, String> colDescription;
    public TableColumn<RecordTM, String> colDate;

    private final RecordBO recordBO = BOFactory.getInstance().getBO(BOTypes.RECORD);

    public TextField descriptionField;
    public TextField vehIdField;
    public DatePicker datePicker;
    public Label recIdValueLabel;

    public AnchorPane ancPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colRecord.setCellValueFactory(new PropertyValueFactory<>("recId"));
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        datePicker.setValue(LocalDate.now());


        try {
            loadNextId();
            loadTableData();
        } catch (Exception e) {
            new Alert(
                    Alert.AlertType.ERROR, "Fail to load data..!"
            ).show();
        }

        recordTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                recIdValueLabel.setText(newSelection.getRecId());
                vehIdField.setText(newSelection.getVehicleId());
                descriptionField.setText(newSelection.getDescription());
                datePicker.setValue(LocalDate.parse(newSelection.getDate()));            }
        });

    }

    private void loadTableData() throws SQLException {
        ArrayList<RecordDto> recordDTOArrayList = recordBO.getAllRecords();

        Collections.reverse(recordDTOArrayList);

        ObservableList<RecordTM> list = FXCollections.observableArrayList();
        for (RecordDto recordDto : recordDTOArrayList) {
            RecordTM recordTM = new RecordTM(
                    recordDto.getRecordId(),
                    recordDto.getVehicleId(),
                    recordDto.getDescription(),
                    recordDto.getDate()
            );
            list.add(recordTM);
        }

        recordTable.setItems(list);
    }


    private void loadNextId() throws SQLException {
        String nextId = recordBO.getNextId();
        recIdValueLabel.setText(nextId);
    }


    private boolean isValid() {
        String vehicleId = vehIdField.getText();
        String description = descriptionField.getText();
        LocalDate date = datePicker.getValue();

        if (!vehicleId.matches("V\\d{3}")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Vehicle ID! Format should be V000.").show();
            return false;
        }

        if (description == null || description.trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Description cannot be empty.").show();
            return false;
        }

        if (date == null) {
            new Alert(Alert.AlertType.ERROR, "Please select a date.").show();
            return false;
        }

        return true;
    }


    public void BtnSaveOnAction(ActionEvent event) {

        if (!isValid()) return;

        String recordId = recIdValueLabel.getText();
        String vehicleId = vehIdField.getText();
        String description = descriptionField.getText();
        String date = datePicker.getValue().toString();

        RecordDto recordDto = new RecordDto(
                recordId,
                vehicleId,
                description,
                date
        );

        try {
            boolean isSave = recordBO.saveRecord(recordDto);
            if (isSave) {
                loadNextId();
                loadTableData();

                vehIdField.setText("");
                descriptionField.setText("");
                datePicker.setValue(LocalDate.now());

                new Alert(
                        Alert.AlertType.INFORMATION, "Record saved successfully..!"
                ).show();
            } else {
                new Alert(
                        Alert.AlertType.ERROR, "Fail to save record..!"
                ).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(
                    Alert.AlertType.ERROR, "Fail to save record..!"
            ).show();
        }
//       Static method call using classname CustomerModel.saveCustomer();

    }




    public void btnDeleteOnAction(ActionEvent event) {
        String recordId = recIdValueLabel.getText();

        try {
            boolean isDeleted = recordBO.deleteRecord(recordId);
            if (isDeleted) {
                loadNextId();
                loadTableData();
                btnResetOnAction(null); // Clear fields

                new Alert(Alert.AlertType.INFORMATION, "Record deleted successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete record.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while deleting record.").show();
        }
    }


    public void btnResetOnAction(ActionEvent event) {
        try {
            loadNextId();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        vehIdField.setText("");
        descriptionField.setText("");
        datePicker.setValue(LocalDate.now());
        recordTable.getSelectionModel().clearSelection();
    }



    public void btnUpdateOnAction(ActionEvent event) {

        if (!isValid()) return;

        String recordId = recIdValueLabel.getText();
        String vehicleId = vehIdField.getText();
        String description = descriptionField.getText();
        String date = datePicker.getValue().toString();

        RecordDto recordDto = new RecordDto(
                recordId,
                vehicleId,
                description,
                date
        );

        try {
            boolean isUpdated = recordBO.updateRecord(recordDto);
            if (isUpdated) {
                loadTableData();
                new Alert(Alert.AlertType.INFORMATION, "Record updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update record.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while updating record.").show();
        }
    }


}
