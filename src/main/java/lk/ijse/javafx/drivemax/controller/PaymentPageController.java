package lk.ijse.javafx.drivemax.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.javafx.drivemax.dto.PaymentDto;
import lk.ijse.javafx.drivemax.dto.tm.PaymentTM;
import lk.ijse.javafx.drivemax.model.PaymentModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class PaymentPageController implements Initializable {

    public TableView<PaymentTM> paymentTable;
    public TableColumn<PaymentTM, String> colPaymentId;
    public TableColumn<PaymentTM, String> colCustomerId;
    public TableColumn<PaymentTM, String> colAmount;
    public TableColumn<PaymentTM, String> colDate;

    private final PaymentModel paymentModel = new PaymentModel();

    public DatePicker datePicker;
    public TextField cusIdField;
    public TextField amountField;
    public Label payIdValueLabel;

    public AnchorPane ancPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("payId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
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

        paymentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                payIdValueLabel.setText(newSelection.getPayId());
                cusIdField.setText(newSelection.getCustomerId());
                amountField.setText(newSelection.getAmount());
                datePicker.setValue(LocalDate.parse(newSelection.getDate()));
            }
        });

    }

    private void loadTableData() throws SQLException {
        ArrayList<PaymentDto> paymentDTOArrayList = paymentModel.getAllPayments();

        Collections.reverse(paymentDTOArrayList);

        ObservableList<PaymentTM> list = FXCollections.observableArrayList();
        for (PaymentDto paymentDto : paymentDTOArrayList) {
            PaymentTM paymentTM = new PaymentTM(
                    paymentDto.getPaymentId(),
                    paymentDto.getCustomerId(),
                    paymentDto.getAmount(),
                    paymentDto.getDate()
            );
               list.add(paymentTM);
        }

        paymentTable.setItems(list);
    }


    private void loadNextId() throws SQLException {
        String nextId = paymentModel.getNextId();
        payIdValueLabel.setText(nextId);
    }

    private boolean isValid() {
        String customerId = cusIdField.getText();
        String amountText = amountField.getText();
        LocalDate date = datePicker.getValue();

        if (!customerId.matches("C\\d{3}")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer ID! Format should be C000.").show();
            return false;
        }

        if (amountText.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Amount cannot be empty.").show();
            return false;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                new Alert(Alert.AlertType.ERROR, "Amount must be greater than zero.").show();
                return false;
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Amount must be a valid number.").show();
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

        String paymentId = payIdValueLabel.getText();
        String customerId = cusIdField.getText();
        String amount = amountField.getText();
        String date = datePicker.getValue().toString();

        PaymentDto paymentDto = new PaymentDto(
                paymentId,
                customerId,
                amount,
                date
        );

        try {
            boolean isSave = paymentModel.savePayment(paymentDto);
            if (isSave) {
                loadNextId();
                loadTableData();

                cusIdField.setText("");
                amountField.setText("");
                datePicker.setValue(LocalDate.now());

                new Alert(
                        Alert.AlertType.INFORMATION, "Payment saved successfully..!"
                ).show();
            } else {
                new Alert(
                        Alert.AlertType.ERROR, "Fail to save payment..!"
                ).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(
                    Alert.AlertType.ERROR, "Fail to save payment..!"
            ).show();
        }
//       Static method call using classname CustomerModel.saveCustomer();

    }




    public void btnDeleteOnAction(ActionEvent event) {
        String paymentId = payIdValueLabel.getText();

        try {
            boolean isDeleted = paymentModel.deletePayment(paymentId);
            if (isDeleted) {
                loadNextId();
                loadTableData();
                btnResetOnAction(null); // Clear fields

                new Alert(Alert.AlertType.INFORMATION, "Payment deleted successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete payment.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while deleting payment.").show();
        }
    }


    public void btnResetOnAction(ActionEvent event) {
        try {
            loadNextId();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cusIdField.setText("");
        amountField.setText("");
        datePicker.setValue(LocalDate.now());
        paymentTable.getSelectionModel().clearSelection();
    }



    public void btnUpdateOnAction(ActionEvent event) {

        if (!isValid()) return;

        String paymentId = payIdValueLabel.getText();
        String customerId = cusIdField.getText();
        String amount = amountField.getText();
        String date = datePicker.getValue().toString();

        PaymentDto paymentDto = new PaymentDto(
                paymentId,
                customerId,
                amount,
                date
        );

        try {
            boolean isUpdated = paymentModel.updatePayment(paymentDto);
            if (isUpdated) {
                loadTableData();
                new Alert(Alert.AlertType.INFORMATION, "payment updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update payment.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while updating payment.").show();
        }
    }


}
