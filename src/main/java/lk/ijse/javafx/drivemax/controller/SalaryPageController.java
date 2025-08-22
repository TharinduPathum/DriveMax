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
import lk.ijse.javafx.drivemax.bo.custom.SalaryBO;
import lk.ijse.javafx.drivemax.dto.SalaryDto;
import lk.ijse.javafx.drivemax.dto.tm.SalaryTM;



import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class SalaryPageController implements Initializable {

    public TextField attendField;
    public Label salIdValuelabel;
    public ComboBox<String> empIdComboBox;
    public TextField otField;
    public TextField dailySalaryField;
    public Label monthlySalaryLabel;
    public DatePicker datePicker;
    public Label empNameLabel;

    private final SalaryBO salaryBO = BOFactory.getInstance().getBO(BOTypes.SALARY);
    private final EmployeeBO employeeBO = BOFactory.getInstance().getBO(BOTypes.EMPLOYEE);


    @FXML private TableView<SalaryTM> salaryTable;
    @FXML private TableColumn<?, ?> colAttend;
    @FXML private TableColumn<?, ?> colDate;
    @FXML private TableColumn<?, ?> colDailySalary;
    @FXML private TableColumn<?, ?> colMonthlySalary;

    @FXML private TableColumn<?, ?> colEmpId;
    @FXML private TableColumn<?, ?> colSalaryId;
    @FXML private TableColumn<?, ?> colOT;

    public AnchorPane ancPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSalaryId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colAttend.setCellValueFactory(new PropertyValueFactory<>("attend"));
        colOT.setCellValueFactory(new PropertyValueFactory<>("ot"));
        colDailySalary.setCellValueFactory(new PropertyValueFactory<>("dsalary"));
        colMonthlySalary.setCellValueFactory(new PropertyValueFactory<>("msalary"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        datePicker.setValue(LocalDate.now());

        loadEmployeeIds();

        try {
            loadNextId();
            loadTableData();
        } catch (Exception e) {
            new Alert(
                    Alert.AlertType.ERROR, "Fail to load data..!"
            ).show();
        }

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

        salaryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                salIdValuelabel.setText(newSelection.getId());
                empIdComboBox.setValue(newSelection.getEmpId());
                attendField.setText(newSelection.getAttend());
                otField.setText(newSelection.getOt());
                dailySalaryField.setText(newSelection.getDsalary());
                monthlySalaryLabel.setText(newSelection.getMsalary());
                datePicker.setValue(LocalDate.parse(newSelection.getDate()));

            }


        });


    }

    private void loadTableData() throws SQLException {

        ArrayList<SalaryDto> salaryDTOArrayList = salaryBO.getAllSalary();
        Collections.reverse(salaryDTOArrayList);
        ObservableList<SalaryTM> list = FXCollections.observableArrayList();

        for (SalaryDto salaryDto : salaryDTOArrayList){
            SalaryTM salaryTM = new SalaryTM(
                    salaryDto.getId(),
                    salaryDto.getEmpId(),
                    salaryDto.getAttend(),
                    salaryDto.getOt(),
                    salaryDto.getDsalary(),
                    salaryDto.getMsalary(),
                    salaryDto.getDate()
            );
            list.add(salaryTM);
        }
        salaryTable.setItems(list);
    }

    private void loadNextId() throws SQLException {
        String nextId = salaryBO.getNextId();
        salIdValuelabel.setText(nextId);


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

    private boolean isValid() {
        String attendText = attendField.getText();
        String otText = otField.getText();
        String dailySalaryText = dailySalaryField.getText();

        if (empIdComboBox.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "Please select an Employee ID!").show();
            return false;
        }

        if (!attendText.matches("\\d+")) {
            new Alert(Alert.AlertType.ERROR, "Attendance must be a positive integer!").show();
            return false;
        }

        if (!otText.matches("\\d+")) {
            new Alert(Alert.AlertType.ERROR, "Overtime hours must be a positive integer!").show();
            return false;
        }

        if (!dailySalaryText.matches("\\d+(\\.\\d{1,2})?")) {
            new Alert(Alert.AlertType.ERROR, "Daily salary must be a valid amount!").show();
            return false;
        }

        return true;
    }



    @FXML
    void BtnSaveOnAction(ActionEvent event) {

        if (!isValid()) return;

        try {
            String salaryId = salIdValuelabel.getText();
            String empId = empIdComboBox.getValue();
            int attend = Integer.parseInt(attendField.getText());
            int ot = Integer.parseInt(otField.getText());
            double dailySalary = Double.parseDouble(dailySalaryField.getText());
            String date = datePicker.getValue().toString();


            final int OVERTIME_RATE = 500;
            double monthlySalary = (dailySalary * attend) + (ot * OVERTIME_RATE);
            String totalSalaryString = String.format("%.2f", monthlySalary);

            SalaryDto salaryDto = new SalaryDto(
                    salaryId,
                    empId,
                    String.valueOf(attend),
                    String.valueOf(ot),
                    String.valueOf(dailySalary),
                    totalSalaryString,
                    date
            );

            boolean isSave = salaryBO.saveSalary(salaryDto);
            if (isSave) {
                loadNextId();
                loadTableData();

                btnResetOnAction(null);

                new Alert(Alert.AlertType.INFORMATION, "Salary saved successfully..!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save salary..!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid number input!").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to save salary..!").show();
        }
    }


    @FXML
    void btnDeleteOnAction(ActionEvent event) {

        String salaryId = salIdValuelabel.getText();

        try {
            boolean isDeleted = salaryBO.deleteSalary(salaryId);
            if (isDeleted) {
                loadNextId();
                loadTableData();
                btnResetOnAction(null);

                new Alert(Alert.AlertType.INFORMATION, "salary deleted successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete salary.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while deleting salary.").show();
        }
    }

    @FXML
    void btnReportOnAction(ActionEvent event) {

    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        try {
            loadNextId();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        empIdComboBox.setValue(null);
        attendField.setText("");
        otField.setText("");
        dailySalaryField.setText("");
        datePicker.setValue(LocalDate.now());
        monthlySalaryLabel.setText("");
        salaryTable.getSelectionModel().clearSelection();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

        if (!isValid()) return;

        String salaryId = salIdValuelabel.getText();
        String empId = empIdComboBox.getValue();
        String attend = attendField.getText();
        String ot = otField.getText();
        String dsalary = dailySalaryField.getText();
        String msalary = monthlySalaryLabel.getText();
        String date = datePicker.getValue().toString();

        SalaryDto salaryDto = new SalaryDto(
                salaryId,
                empId,
                attend,
                ot,
                dsalary,
                msalary,
                date
        );

        try {
            boolean isUpdated = salaryBO.updateSalary(salaryDto);
            if (isUpdated) {
                loadTableData();
                new Alert(Alert.AlertType.INFORMATION, "salary updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update salary.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while updating salary.").show();
        }
    }


}
