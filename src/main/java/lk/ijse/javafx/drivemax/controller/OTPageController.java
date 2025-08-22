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
import lk.ijse.javafx.drivemax.bo.custom.EmployeeBO;
import lk.ijse.javafx.drivemax.bo.custom.InvoiceBO;
import lk.ijse.javafx.drivemax.bo.custom.OTBO;
import lk.ijse.javafx.drivemax.dto.OTDto;
import lk.ijse.javafx.drivemax.dto.tm.CustomerTM;
import lk.ijse.javafx.drivemax.dto.tm.OTTM;



import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class OTPageController implements Initializable {

    @FXML
    private TableView<OTTM> otTable;
    @FXML
    private TableColumn<OTTM, String> colEmpId;
    @FXML
    private TableColumn<OTTM, String> colDate;
    @FXML
    private TableColumn<OTTM, String> colHours;

    @FXML
    private ComboBox<String> empIdComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> hourComboBox;
    @FXML
    private Label empNameLabel;

    @FXML
    public AnchorPane ancPane;

    private final OTBO otbo = BOFactory.getInstance().getBO(BOTypes.OT);
    private final EmployeeBO employeeBO = BOFactory.getInstance().getBO(BOTypes.EMPLOYEE);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colHours.setCellValueFactory(new PropertyValueFactory<>("hours"));

        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 0; i <= 6; i++) {
            hours.add(String.valueOf(i));
        }
        hourComboBox.setItems(hours);

        datePicker.setValue(LocalDate.now());
        loadEmployeeIds();

        try {
            loadTableData();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load OT data!").show();
        }

        otTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                empIdComboBox.setValue(newSel.getEmpId());
                datePicker.setValue(LocalDate.parse(newSel.getDate()));
                hourComboBox.setValue(newSel.getHours());
            }
        });

        empIdComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    String empName = employeeBO.getEmployeeName(newVal);
                    empNameLabel.setText(empName != null ? empName : "Name not found");
                } catch (SQLException e) {
                    e.printStackTrace();
                    empNameLabel.setText("Error loading name");
                }
            } else {
                empNameLabel.setText("");
            }
        });
    }

    private void loadEmployeeIds() {
        try {
            ArrayList<String> ids = employeeBO.getAllEmployeeIds();
            empIdComboBox.setItems(FXCollections.observableArrayList(ids));
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load employee IDs!").show();
        }
    }

    private void loadTableData() throws SQLException {
        otTable.setItems(FXCollections.observableArrayList(
                otbo.getAllOT().stream().map(otDto ->
                        new OTTM(
                                otDto.getEmpId(),
                                otDto.getDate(),
                                otDto.getHours()
                        )).toList()
        ));
    }

    public void BtnSaveOnAction(ActionEvent actionEvent) {
        String empId = empIdComboBox.getValue();
        String date = datePicker.getValue().toString();
        String hours = hourComboBox.getValue();

        if (empId == null || hours == null) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields.").show();
            return;
        }

        OTDto dto = new OTDto(empId, date, hours);

        try {
            boolean isSaved = otbo.saveOT(dto);
            if (isSaved) {
                loadTableData();
                btnResetOnAction(null);
                new Alert(Alert.AlertType.INFORMATION, "OT saved successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save OT.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while saving OT.").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent event) {
        String empId = empIdComboBox.getValue();
        String date = datePicker.getValue().toString();

        try {
            boolean isDeleted = otbo.deleteOT(empId, date);
            if (isDeleted) {
                loadTableData();
                btnResetOnAction(null);
                new Alert(Alert.AlertType.INFORMATION, "OT deleted successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete OT.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while deleting OT.").show();
        }
    }

    public void btnUpdateOnAction(ActionEvent event) {
        String empId = empIdComboBox.getValue();
        String date = datePicker.getValue().toString();
        String hours = hourComboBox.getValue();

        OTDto dto = new OTDto(empId, date, hours);

        try {
            boolean isUpdated = otbo.updateOT(dto);
            if (isUpdated) {
                loadTableData();
                new Alert(Alert.AlertType.INFORMATION, "OT updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update OT.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while updating OT.").show();
        }
    }

    public void btnResetOnAction(ActionEvent event) {
        empIdComboBox.setValue(null);
        datePicker.setValue(LocalDate.now());
        hourComboBox.setValue(null);
        otTable.getSelectionModel().clearSelection();
    }
}
