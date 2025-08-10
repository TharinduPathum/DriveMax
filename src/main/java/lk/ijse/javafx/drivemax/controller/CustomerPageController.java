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
import lk.ijse.javafx.drivemax.db.DBConnection;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.dto.tm.CustomerTM;
import lk.ijse.javafx.drivemax.model.CustomerModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class CustomerPageController implements Initializable {
    public Label custidValueLabel;
    public TextField nameField;
    public TextField addressField;
    public TextField emailField;
    public TextField phoneNoField;

    private final CustomerModel customerModel = new CustomerModel();

    public TableView<CustomerTM> customerTable;
    public TableColumn<CustomerTM, String> colId;
    public TableColumn<CustomerTM, String> colName;
    public TableColumn<CustomerTM, String> colAddress;
    public TableColumn<CustomerTM, String> colEmail;
    public TableColumn<CustomerTM, String> colPhoneNo;

    public AnchorPane ancPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
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

        customerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                custidValueLabel.setText(newSelection.getId());
                nameField.setText(newSelection.getName());
                addressField.setText(newSelection.getAddress());
                emailField.setText(newSelection.getEmail());
                phoneNoField.setText(newSelection.getPhone());
            }
        });

    }

    private void loadTableData() throws SQLException {
        ArrayList<CustomerDto> customerDTOArrayList = customerModel.getAllCustomer();

        Collections.reverse(customerDTOArrayList);

        ObservableList<CustomerTM> list = FXCollections.observableArrayList();
        for (CustomerDto customerDto : customerDTOArrayList) {
            CustomerTM customerTM = new CustomerTM(
                    customerDto.getCustomerId(),
                    customerDto.getName(),
                    customerDto.getAddress(),
                    customerDto.getEmail(),
                    customerDto.getPhone()
            );
            list.add(customerTM);
        }

        customerTable.setItems(list);
    }


    private void loadNextId() throws SQLException {
        String nextId = customerModel.getNextId();
        custidValueLabel.setText(nextId);
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

       String customerId = custidValueLabel.getText();
       String name = nameField.getText();
       String address = addressField.getText();
       String email = emailField.getText();
       String phone = phoneNoField.getText();

       CustomerDto customerDto = new CustomerDto(
               customerId,
               name,
               address,
               email,
               phone
       );

            try {
            boolean isSave = customerModel.saveCustomer(customerDto);
            if (isSave) {
                loadNextId();
                loadTableData();

                nameField.setText("");
                addressField.setText("");
                emailField.setText("");
                phoneNoField.setText("");

                new Alert(
                        Alert.AlertType.INFORMATION, "Customer saved successfully..!"
                ).show();
            } else {
                new Alert(
                        Alert.AlertType.ERROR, "Fail to save customer..!"
                ).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(
                    Alert.AlertType.ERROR, "Fail to save customer..!"
            ).show();
        }

    }




   public void btnDeleteOnAction(ActionEvent event) {
        String customerId = custidValueLabel.getText();

        try {
            boolean isDeleted = customerModel.deleteCustomer(customerId);
            if (isDeleted) {
                loadNextId();
                loadTableData();
                btnResetOnAction(null); // Clear fields

                new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete customer.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while deleting customer.").show();
        }
    }


    @FXML
    void btnReportOnAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/report/Blank_A4_1.jrxml")

            );

            Connection connection = DBConnection.getInstance().getConnection();

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("p_date", LocalDate.now().toString());

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    connection
            );

            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
        }

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

        customerTable.getSelectionModel().clearSelection();
    }



    public void btnUpdateOnAction(ActionEvent event) {

        if (!isValidInput()) return;

        String customerId = custidValueLabel.getText();
        String name = nameField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String phone = phoneNoField.getText();

        CustomerDto customerDto = new CustomerDto(
                customerId,
                name,
                address,
                email,
                phone
        );

        try {
            boolean isUpdated = customerModel.updateCustomer(customerDto);
            if (isUpdated) {
                loadTableData();
                new Alert(Alert.AlertType.INFORMATION, "Customer updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update customer.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while updating customer.").show();
        }
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

    @FXML
    void btnAnnounceOnAction(ActionEvent actionEvent) {
        navigateTo("/view/EmailPage.fxml");
    }

    @FXML
    void btnFeedbackOnAction(ActionEvent event) {

    }

    @FXML
    void btnInvoiceOnAction(ActionEvent event) {
          navigateTo("/view/InvoicePage.fxml");
    }

    @FXML
    void btnPaymentOnAction(ActionEvent event) {
      navigateTo("/view/PaymentPage.fxml");
    }






}



