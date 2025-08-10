package lk.ijse.javafx.drivemax.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.javafx.drivemax.dto.AttendanceDto;
import lk.ijse.javafx.drivemax.dto.tm.AttendanceTM;
import lk.ijse.javafx.drivemax.model.AttendanceModel;
import lk.ijse.javafx.drivemax.model.EmployeeModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class AttendancePageController implements Initializable {

    @FXML
    public TableView<AttendanceTM> attendanceTable;
    @FXML
    public TableColumn<AttendanceTM, String> colEmpId;
    @FXML
    public TableColumn<AttendanceTM, String> colDate;
    @FXML
    public TableColumn<AttendanceTM, String> colStatus;

    @FXML
    public ComboBox<String> empIdComboBox;
    @FXML
    public DatePicker datePicker;
    @FXML
    public ComboBox<String> statusComboBox;
    @FXML
    private Label empNameLabel;


    @FXML
    public AnchorPane ancPane;

    private final AttendanceModel attendanceModel = new AttendanceModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        statusComboBox.setItems(FXCollections.observableArrayList("Present", "Absent"));
        datePicker.setValue(LocalDate.now());

        loadEmployeeIds();

        try {
            loadTableData();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load attendance data!").show();
        }

        attendanceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                empIdComboBox.setValue(newSelection.getId());
                datePicker.setValue(LocalDate.parse(newSelection.getDate()));
                statusComboBox.setValue(newSelection.getStatus());
            }
        });

        empIdComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    String empName = employeeModel.getEmployeeName(newVal);
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

    private void loadTableData() throws SQLException {
        ArrayList<AttendanceDto> attendanceList = attendanceModel.getAllAttendance();

        Collections.reverse(attendanceList);

        ObservableList<AttendanceTM> observableList = FXCollections.observableArrayList();
        for (AttendanceDto dto : attendanceList) {
            observableList.add(new AttendanceTM(dto.getEmpId(), dto.getDate(), dto.getStatus()));
        }

        attendanceTable.setItems(observableList);
    }

    private final EmployeeModel employeeModel = new EmployeeModel();

    private void loadEmployeeIds() {
        try {
            ArrayList<String> ids = employeeModel.getAllEmployeeIds();
            empIdComboBox.setItems(FXCollections.observableArrayList(ids));
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load employee IDs!").show();
        }
    }


    public void BtnSaveOnAction(ActionEvent actionEvent) {
        String empId = empIdComboBox.getValue();
        String date = datePicker.getValue().toString();
        String status = statusComboBox.getValue();

        if (empId.isEmpty() || status == null) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields.").show();
            return;
        }

        AttendanceDto dto = new AttendanceDto(empId, date, status);

        try {
            boolean isSaved = attendanceModel.saveAttendance(dto);
            if (isSaved) {
                loadTableData();
                btnResetOnAction(null);
                new Alert(Alert.AlertType.INFORMATION, "Attendance saved successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save attendance.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while saving attendance.").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent event) {
        String empId = empIdComboBox.getValue();
        String date = datePicker.getValue().toString();

        try {
            boolean isDeleted = attendanceModel.deleteAttendance(empId, date);
            if (isDeleted) {
                loadTableData();
                btnResetOnAction(null);
                new Alert(Alert.AlertType.INFORMATION, "Attendance deleted successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete attendance.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while deleting attendance.").show();
        }
    }

    public void btnUpdateOnAction(ActionEvent event) {
        String empId = empIdComboBox.getValue();
        String date = datePicker.getValue().toString();
        String status = statusComboBox.getValue();

        AttendanceDto dto = new AttendanceDto(empId, date, status);

        try {
            boolean isUpdated = attendanceModel.updateAttendance(dto);
            if (isUpdated) {
                loadTableData();
                new Alert(Alert.AlertType.INFORMATION, "Attendance updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update attendance.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while updating attendance.").show();
        }
    }

    public void btnResetOnAction(ActionEvent event) {
        empIdComboBox.setValue(null);
        datePicker.setValue(LocalDate.now());
        statusComboBox.setValue(null);
        attendanceTable.getSelectionModel().clearSelection();
    }


}
