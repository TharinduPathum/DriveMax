package lk.ijse.javafx.drivemax.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.javafx.drivemax.bo.BOFactory;
import lk.ijse.javafx.drivemax.bo.BOTypes;
import lk.ijse.javafx.drivemax.bo.custom.InvoiceBO;
import lk.ijse.javafx.drivemax.dto.InvoiceDto;
import lk.ijse.javafx.drivemax.dto.tm.InvoiceTM;


import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class InvoicePageController implements Initializable {

    @FXML
    private TableColumn<InvoiceTM, String> colCustomerId;

    @FXML
    private TableColumn<InvoiceTM, String> colDate;

    @FXML
    private TableColumn<InvoiceTM, String> colDescription;

    @FXML
    private TableColumn<InvoiceTM, String> colInvoiceId;

    @FXML
    private TableColumn<InvoiceTM, String> colPaymentId;

    @FXML
    private TextField custIdField;

    @FXML
    private Label custNameLabel;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField descriptionField;

    @FXML
    private Label invoiceIdLabel;

    @FXML
    private TableView<InvoiceTM> invoiceTable;

    @FXML
    private TextField payIdField;

    private final InvoiceBO invoiceBO = BOFactory.getInstance().getBO(BOTypes.INVOICE);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colInvoiceId.setCellValueFactory(new PropertyValueFactory<>("invId"));
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));


        datePicker.setValue(LocalDate.now());


        try {
            loadNextId();
            loadTableData();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load data!").show();
        }

        invoiceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                invoiceIdLabel.setText(newVal.getInvId());
                payIdField.setText(newVal.getPaymentId());
                custIdField.setText(newVal.getCustomerId());
                descriptionField.setText(newVal.getDescription());
                datePicker.setValue(LocalDate.parse(newVal.getDate()));
            }
        });
    }

    private void loadNextId() throws SQLException {
        String nextId = invoiceBO.getNextId();
        invoiceIdLabel.setText(nextId);
    }

    private void loadTableData() throws SQLException {
        ArrayList<InvoiceDto> list = invoiceBO.getAllInvoices();
        Collections.reverse(list);

        ObservableList<InvoiceTM> observableList = FXCollections.observableArrayList();
        for (InvoiceDto invoiceDto: list) {
            observableList.add(new InvoiceTM(
                    invoiceDto.getInvoiceId(),
                    invoiceDto.getPaymentId(),
                    invoiceDto.getCustomerId(),
                    invoiceDto.getDescription(),
                    invoiceDto.getDate()
            ));
        }

        invoiceTable.setItems(observableList);
    }

    private boolean isValid() {
        String paymentId = payIdField.getText();
        String customerId = custIdField.getText();
        String description = descriptionField.getText();
        LocalDate date = datePicker.getValue();

        if (!paymentId.matches("P\\d{3}")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Payment ID! It should be in the format P000").show();
            return false;
        }

        if (!customerId.matches("C\\d{3}")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer ID! It should be in the format C000").show();
            return false;
        }

        if (description.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Description cannot be empty!").show();
            return false;
        }

        if (date == null) {
            new Alert(Alert.AlertType.ERROR, "Please select a date!").show();
            return false;
        }

        return true;
    }


    @FXML
    void BtnSaveOnAction(ActionEvent event) {

        if (!isValid()) return;

        String invoiceId = invoiceIdLabel.getText();
        String paymentId = payIdField.getText();
        String customerId = custIdField.getText();
        String description = descriptionField.getText();
        String date = datePicker.getValue().toString();

        InvoiceDto invoiceDto = new InvoiceDto(
                invoiceId,
                paymentId,
                customerId,
                description,
                date);

        try {
            boolean isSaved = invoiceBO.saveInvoice(invoiceDto);
            if (isSaved) {
                loadNextId();
                loadTableData();
                btnResetOnAction(null);
                new Alert(Alert.AlertType.INFORMATION, "Invoice saved successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save invoice!").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while saving invoice!").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String invoiceId = invoiceIdLabel.getText();

        try {
            boolean isDeleted = invoiceBO.deleteInvoice(invoiceId);
            if (isDeleted) {
                loadNextId();
                loadTableData();
                btnResetOnAction(null);
                new Alert(Alert.AlertType.INFORMATION, "Invoice deleted successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete invoice!").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while deleting invoice!").show();
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        try {
            loadNextId();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        payIdField.clear();
        custIdField.clear();
        descriptionField.clear();
        datePicker.setValue(null);
        invoiceTable.getSelectionModel().clearSelection();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

        if (!isValid()) return;

        String invoiceId = invoiceIdLabel.getText();
        String paymentId = payIdField.getText();
        String customerId = custIdField.getText();
        String description = descriptionField.getText();
        String date = datePicker.getValue().toString();

        InvoiceDto invoiceDto = new InvoiceDto(
                invoiceId,
                customerId,
                paymentId,
                description,
                date);

        try {
            boolean isUpdated = invoiceBO.updateInvoice(invoiceDto);
            if (isUpdated) {
                loadTableData();
                new Alert(Alert.AlertType.INFORMATION, "Invoice updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update invoice!").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while updating invoice!").show();
        }
    }
}
