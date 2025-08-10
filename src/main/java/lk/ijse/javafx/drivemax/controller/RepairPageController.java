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
import lk.ijse.javafx.drivemax.dto.RepairDto;
import lk.ijse.javafx.drivemax.dto.tm.RepairTM;
import lk.ijse.javafx.drivemax.model.RepairModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class RepairPageController implements Initializable {

    public TableView<RepairTM> repairTable;
    public TableColumn<RepairTM, String> colDate;
    public TableColumn<RepairTM, String> colEmployeeId;
    public TableColumn<RepairTM, String> colRepairId;
    public TableColumn<RepairTM, String> colVehicleId;
    public TableColumn<RepairTM, String> colWork;
    public TableColumn<RepairTM, String> colCost;


    private final RepairModel repairModel = new RepairModel();

    public DatePicker datePicker;
    public TextField empField;
    public Label repidValueLabel;
    public TextField vehIdField;
    public TextField workField;
    public TextField costField;

    public AnchorPane ancPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colRepairId.setCellValueFactory(new PropertyValueFactory<>("repId"));
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colWork.setCellValueFactory(new PropertyValueFactory<>("work"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
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

        repairTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                repidValueLabel.setText(newSelection.getRepId());
                vehIdField.setText(newSelection.getVehicleId());
                empField.setText(newSelection.getEmployeeId());
                workField.setText(newSelection.getWork());
                costField.setText(newSelection.getCost());
                datePicker.setValue(LocalDate.parse(newSelection.getDate()));            }
        });

    }

    private void loadTableData() throws SQLException {
        ArrayList<RepairDto> repairDTOArrayList = repairModel.getAllRepairs();

        Collections.reverse(repairDTOArrayList);

        ObservableList<RepairTM> list = FXCollections.observableArrayList();
        for (RepairDto repairDto : repairDTOArrayList) {
            RepairTM repairTM = new RepairTM(
                    repairDto.getRepairId(),
                    repairDto.getVehicleId(),
                    repairDto.getEmployeeId(),
                    repairDto.getWork(),
                    repairDto.getCost(),
                    repairDto.getDate()
            );
            list.add(repairTM);
        }

        repairTable.setItems(list);
    }


    private void loadNextId() throws SQLException {
        String nextId = repairModel.getNextId();
        repidValueLabel.setText(nextId);
    }

    private boolean isValid() {
        String vehicleId = vehIdField.getText();
        String employeeId = empField.getText();
        String work = workField.getText();
        String cost = costField.getText();
        LocalDate date = datePicker.getValue();

        if (!vehicleId.matches("V\\d{3}")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Vehicle ID! Format should be V000.").show();
            return false;
        }

        if (!employeeId.matches("E\\d{3}")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee ID! Format should be E000.").show();
            return false;
        }

        if (work == null || work.trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Work description cannot be empty.").show();
            return false;
        }

        if (!cost.matches("\\d+(\\.\\d{1,2})?")) {
            new Alert(Alert.AlertType.ERROR, "Invalid cost! Must be a valid number.").show();
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

        String repairId = repidValueLabel.getText();
        String vehicleId = vehIdField.getText();
        String employeeId = empField.getText();
        String work = workField.getText();
        String cost = costField.getText();
        String date = datePicker.getValue().toString();

        RepairDto repairDto = new RepairDto(
                repairId,
                vehicleId,
                employeeId,
                work,
                cost,
                date
        );

        try {
            boolean isSave = repairModel.saveRepair(repairDto);
            if (isSave) {
                loadNextId();
                loadTableData();

                vehIdField.setText("");
                empField.setText("");
                workField.setText("");
                costField.setText("");
                datePicker.setValue(LocalDate.now());

                new Alert(
                        Alert.AlertType.INFORMATION, "Repair work saved successfully..!"
                ).show();
            } else {
                new Alert(
                        Alert.AlertType.ERROR, "Fail to save repair work..!"
                ).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(
                    Alert.AlertType.ERROR, "Fail to save repair work..!"
            ).show();
        }
//       Static method call using classname CustomerModel.saveCustomer();

    }




    public void btnDeleteOnAction(ActionEvent event) {
        String repairId = repidValueLabel.getText();

        try {
            boolean isDeleted = repairModel.deleteRepair(repairId);
            if (isDeleted) {
                loadNextId();
                loadTableData();
                btnResetOnAction(null); // Clear fields

                new Alert(Alert.AlertType.INFORMATION, "Repair work deleted successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete repair work.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while deleting repair work.").show();
        }
    }

    public void btnResetOnAction(ActionEvent event) {
        try {
            loadNextId();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        vehIdField.setText("");
        empField.setText("");
        workField.setText("");
        costField.setText("");
        datePicker.setValue(LocalDate.now());

        repairTable.getSelectionModel().clearSelection();
    }



    public void btnUpdateOnAction(ActionEvent event) {

        if (!isValid()) return;

        String repairId = repidValueLabel.getText();
        String vehicleId = vehIdField.getText();
        String employeeId = empField.getText();
        String work = workField.getText();
        String cost = costField.getText();
        String date = datePicker.getValue().toString();

        RepairDto repairDto = new RepairDto(
                repairId,
                vehicleId,
                employeeId,
                work,
                cost,
                date
        );

        try {
            boolean isUpdated = repairModel.updateRepair(repairDto);
            if (isUpdated) {
                loadTableData();
                new Alert(Alert.AlertType.INFORMATION, "Repair work updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update repair work.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while updating repair work.").show();
        }
    }


}
