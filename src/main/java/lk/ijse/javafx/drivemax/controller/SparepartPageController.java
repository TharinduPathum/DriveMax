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
import lk.ijse.javafx.drivemax.bo.custom.SparepartBO;
import lk.ijse.javafx.drivemax.dto.SparepartDto;
import lk.ijse.javafx.drivemax.dto.tm.SparepartTM;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class SparepartPageController implements Initializable {

    public DatePicker datePicker;
    public TextField repairField;
    public TextField sparepartField;

    private final SparepartBO sparepartBO = BOFactory.getInstance().getBO(BOTypes.SPAREPART);

    public TableView<SparepartTM> sparePartTable;
    public TableColumn<SparepartTM, String> colSparePart;
    public TableColumn<SparepartTM, String> colRepair;
    public TableColumn<SparepartTM, String> colDate;

    public AnchorPane ancPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSparePart.setCellValueFactory(new PropertyValueFactory<>("spId"));
        colRepair.setCellValueFactory(new PropertyValueFactory<>("repair"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        datePicker.setValue(LocalDate.now());

        try {

           loadTableData();
        } catch (Exception e) {
            new Alert(
                    Alert.AlertType.ERROR, "Fail to load data..!"
            ).show();
        }

        sparePartTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                sparepartField.setText(newSelection.getSpId());
                repairField.setText(newSelection.getRepair());
                datePicker.setValue(LocalDate.parse(newSelection.getDate()));
            }
        });

    }

    private void loadTableData() throws SQLException {
        ArrayList<SparepartDto> sparepartDTOArrayList = sparepartBO.getAllSparePartUsages();

        Collections.reverse(sparepartDTOArrayList);

        ObservableList<SparepartTM> list = FXCollections.observableArrayList();
        for (SparepartDto sparepartDto : sparepartDTOArrayList) {
            SparepartTM sparepartTM = new SparepartTM(
                    sparepartDto.getSpId(),
                    sparepartDto.getRepair(),
                    sparepartDto.getDate()

            );
            list.add(sparepartTM);
        }

        sparePartTable.setItems(list);
    }


    private boolean isValid() {
        String repair = repairField.getText();

        if (repair == null || repair.isBlank()) {
            new Alert(Alert.AlertType.ERROR, "Repair ID cannot be empty!").show();
            return false;
        }

        if (!repair.matches("REP\\d{3}")) {
            new Alert(Alert.AlertType.ERROR, "Repair ID must be in the format 'REP000' (e.g. REP001)!").show();
            return false;
        }

        return true;
    }


    public void BtnSaveOnAction(ActionEvent event) {
        if (!isValid()) return;

        String sparePartId = sparepartField.getText();
        String repair = repairField.getText();
        String date = datePicker.getValue().toString();

        SparepartDto sparepartDto = new SparepartDto(sparePartId, repair, date);

        try {
            boolean isSaved = sparepartBO.saveSparePartUsageWithInventoryUpdate(sparepartDto);
            if (isSaved) {
                loadTableData();
                btnResetOnAction(null);
                new Alert(Alert.AlertType.INFORMATION, "Sparepart usage saved successfully!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Save failed: Check inventory quantity or duplicate entry.").show();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // You can log this properly in real apps
            new Alert(Alert.AlertType.ERROR, "Database error occurred while saving spare part usage.").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred.").show();
        }
    }





    public void btnDeleteOnAction(ActionEvent event) {
        String sparePartId = sparepartField.getText();

        try {
            boolean isDeleted = sparepartBO.deleteSparePartUsage(sparePartId);
            if (isDeleted) {

                loadTableData();
                btnResetOnAction(null); // Clear fields

                new Alert(Alert.AlertType.INFORMATION, "Spare part usage deleted successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete spare part usage.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while deleting spare part usage.").show();
        }
    }


    public void btnResetOnAction(ActionEvent event) {

        sparepartField.setText("");
        repairField.setText("");
        datePicker.setValue(LocalDate.now());

        sparePartTable.getSelectionModel().clearSelection();
    }



    public void btnUpdateOnAction(ActionEvent event) {
        if (!isValid()) return;

        String sparePartId = sparepartField.getText();
        String repair = repairField.getText();
        String date = datePicker.getValue().toString();

        if (sparePartId == null || sparePartId.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Please select a spare part usage from the table to update.").show();
            return;
        }

        SparepartDto sparepartDto = new SparepartDto(sparePartId, repair, date);

        try {
            boolean isUpdated = sparepartBO.updateSparePartUsage(sparepartDto);
            if (isUpdated) {
                loadTableData();
                btnResetOnAction(null);
                new Alert(Alert.AlertType.INFORMATION, "Spare part usage updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Update failed. Record may not exist.").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error occurred during update.").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred during update.").show();
        }
    }




}
