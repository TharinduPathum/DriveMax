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
import lk.ijse.javafx.drivemax.dto.EmployeeDto;
import lk.ijse.javafx.drivemax.dto.tm.EmployeeTM;
import lk.ijse.javafx.drivemax.model.EmployeeModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class EmployeePageController implements Initializable {


    public TextField addressField;
    public AnchorPane ancPane;
    public Label empidValueLabel;
    public TextField emailField;
    public TextField nameField;
    public TextField phoneNoField;
    public TextField specialityField;

    private final EmployeeModel employeeModel = new EmployeeModel();
    public TableView<EmployeeTM> employeeTable;
    public TableColumn<EmployeeTM, String> colId;
    public TableColumn<EmployeeTM, String> colName;
    public TableColumn<EmployeeTM, String> colSpeciality;
    public TableColumn<EmployeeTM, String> colAddress;
    public TableColumn<EmployeeTM, String> colEmail;
    public TableColumn<EmployeeTM, String> colPhoneNo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSpeciality.setCellValueFactory(new PropertyValueFactory<>("speciality"));
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

        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                empidValueLabel.setText(newSelection.getId());
                nameField.setText(newSelection.getName());
                specialityField.setText(newSelection.getSpeciality());
                addressField.setText(newSelection.getAddress());
                emailField.setText(newSelection.getEmail());
                phoneNoField.setText(newSelection.getPhone());

            }
        });

    }

    private void loadTableData() throws SQLException {
        // 1. Long code
        ArrayList<EmployeeDto> employeeDTOArrayList = employeeModel.getAllEmployee();
        Collections.reverse(employeeDTOArrayList);
        ObservableList<EmployeeTM> list = FXCollections.observableArrayList();

        for (EmployeeDto employeeDto : employeeDTOArrayList){
            EmployeeTM employeeTM = new EmployeeTM(
                    employeeDto.getEmployeeId(),
                    employeeDto.getName(),
                    employeeDto.getSpeciality(),
                    employeeDto.getAddress(),
                    employeeDto.getEmail(),
                    employeeDto.getPhone()
            );
            list.add(employeeTM);
        }
        employeeTable.setItems(list);
    }

    private void loadNextId() throws SQLException {
        String nextId = employeeModel.getNextId();
        empidValueLabel.setText(nextId);
    }

    private boolean isValidInput() {
        String name = nameField.getText().trim();
        String speciality = specialityField.getText().trim();
        String address = addressField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneNoField.getText().trim();

        if (!name.matches("[A-Za-z ]+")) {
            showAlert("Invalid Name! Only letters and spaces allowed.");
            return false;
        }

        if (!speciality.matches("[A-Za-z ]+")) {
            showAlert("Invalid Speciality! Only letters and spaces allowed.");
            return false;
        }

        if (!address.matches("[A-Za-z ]+")) {
            showAlert("Invalid Address! Only letters and spaces allowed.");
            return false;
        }

        if (!email.matches("^[\\w.-]+@gmail\\.com$") && !email.matches("^[\\w.-]+@yahoo\\.com$")) {
            showAlert("Invalid Email! Only @gmail.com or @yahoo.com addresses are allowed.");
            return false;
        }

        if (!phone.matches("\\d{10}")) {
            showAlert("Invalid Phone Number! Must be exactly 10 digits.");
            return false;
        }

        return true;
    }

    private void showAlert(String message) {
        new Alert(Alert.AlertType.WARNING, message).show();
    }



    public void BtnSaveOnAction(ActionEvent event) {

        if (!isValidInput()) return;

        String employeeId = empidValueLabel.getText();
        String name = nameField.getText();
        String speciality = specialityField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String phone = phoneNoField.getText();

        EmployeeDto employeeDto = new EmployeeDto(
                employeeId,
                name,
                speciality,
                address,
                email,
                phone
        );


        try {
            boolean isSave = employeeModel.saveEmployee(employeeDto);
            if (isSave) {
                loadNextId();
                loadTableData();

                nameField.setText("");
                specialityField.setText("");
                addressField.setText("");
                emailField.setText("");
                phoneNoField.setText("");

                new Alert(
                        Alert.AlertType.INFORMATION, "Employee saved successfully..!"
                ).show();
            } else {
                new Alert(
                        Alert.AlertType.ERROR, "Fail to save employee..!"
                ).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(
                    Alert.AlertType.ERROR, "Fail to save employee..!"
            ).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String empId = empidValueLabel.getText();

        try {
            boolean isDeleted = employeeModel.deleteEmployee(empId);
            if (isDeleted) {
                loadNextId();
                loadTableData();
                btnResetOnAction(null);
                new Alert(Alert.AlertType.INFORMATION, "Employee deleted successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete employee.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while deleting employee.").show();
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

        nameField.setText("");
        specialityField.setText("");
        addressField.setText("");
        emailField.setText("");
        phoneNoField.setText("");

        employeeTable.getSelectionModel().clearSelection();
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {

        if (!isValidInput()) return;

        String employeeId = empidValueLabel.getText();
        String name = nameField.getText();
        String speciality = specialityField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String phone = phoneNoField.getText();

        EmployeeDto employeeDto = new EmployeeDto(
                employeeId,
                name,
                speciality,
                address,
                email,
                phone
        );

        try {
            boolean isUpdated = employeeModel.updateEmployee(employeeDto);
            if (isUpdated) {
                loadTableData();
                new Alert(Alert.AlertType.INFORMATION, "Employee updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update employee.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while updating employee.").show();
        }
    }


    public void btnAnnounceOnAction(ActionEvent actionEvent) {
        navigateTo("/view/EmailPage.fxml");

    }

    @FXML
    void btnOTOnAction(ActionEvent event) {
        navigateTo("/view/OTPage.fxml");
    }

    @FXML
    void btnAttendOnAction(ActionEvent event) {
        navigateTo("/view/AttendancePage.fxml");
    }

    @FXML
    void btnSalaryOnAction(ActionEvent event) {
         navigateTo("/view/SalaryPage.fxml");
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
