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
import lk.ijse.javafx.drivemax.bo.BOFactory;
import lk.ijse.javafx.drivemax.bo.BOTypes;
import lk.ijse.javafx.drivemax.bo.custom.CustomerBO;
import lk.ijse.javafx.drivemax.bo.custom.InventoryBO;
import lk.ijse.javafx.drivemax.db.DBConnection;
import lk.ijse.javafx.drivemax.dto.InventoryDto;
import lk.ijse.javafx.drivemax.dto.tm.CustomerTM;
import lk.ijse.javafx.drivemax.dto.tm.InventoryTM;
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
import java.util.stream.Collectors;

public class InventoryPageController implements Initializable {

    public TextField amountField;
    public AnchorPane ancPane;
    public TextField brandField;
    public TextField nameField;
    public TextField quantityField;
    public Label spidValueLabel;
    public TextField supIdField;

    private final InventoryBO inventoryBO = BOFactory.getInstance().getBO(BOTypes.INVENTORY);

    public TableView<InventoryTM> sparePartTable;
    public TableColumn<InventoryTM, String> colSpId;
    public TableColumn<InventoryTM, String> colSupId;
    public TableColumn<InventoryTM, String> colBrand;
    public TableColumn<InventoryTM, String> colName;
    public TableColumn<InventoryTM, String> colAmount;
    public TableColumn<InventoryTM, String> colQuantity;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSpId.setCellValueFactory(new PropertyValueFactory<>("spId"));
        colSupId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));


        try {
            loadNextId();
            loadTableData();
        } catch (Exception e) {
            new Alert(
                    Alert.AlertType.ERROR, "Fail to load data..!"
            ).show();
        }

        sparePartTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                spidValueLabel.setText(newSelection.getSpId());
                supIdField.setText(newSelection.getSupplierId());
                brandField.setText(newSelection.getBrand());
                nameField.setText(newSelection.getName());
                amountField.setText(newSelection.getAmount());
                quantityField.setText(newSelection.getQuantity());
            }
        });


    }



    private void loadTableData() throws SQLException {


        sparePartTable.setItems(FXCollections.observableArrayList(
                inventoryBO.getAllSparePart().stream()
                        .map(inventoryDto ->
                                new InventoryTM(
                                inventoryDto.getSpId(),
                                inventoryDto.getSupplierId(),
                                inventoryDto.getBrand(),
                                inventoryDto.getName(),
                                inventoryDto.getAmount(),
                                inventoryDto.getQuantity()
                        )).toList()


        ));


    }

    private void loadNextId() throws SQLException {
        String nextId = inventoryBO.getNextId();
        spidValueLabel.setText(nextId);
    }

    private boolean isValid() {
        String supplierId = supIdField.getText();
        String brand = brandField.getText();
        String name = nameField.getText();
        String amount = amountField.getText();
        String quantity = quantityField.getText();

        if (!supplierId.matches("SUP\\d{3}")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Supplier ID! It should be in the format SUP000").show();
            return false;
        }

        if (!brand.matches("[A-Za-z ]+")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Brand! Only letters and spaces are allowed").show();
            return false;
        }

        if (!name.matches("[A-Za-z ]+")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Name! Only letters and spaces are allowed").show();
            return false;
        }

        if (!amount.matches("\\d+(\\.\\d{1,2})?")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Amount! It should be a number (e.g., 100 or 99.99)").show();
            return false;
        }

        if (!quantity.matches("\\d+")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Quantity! Only digits are allowed").show();
            return false;
        }

        return true;
    }


    public void BtnSaveOnAction(ActionEvent event) {

        if (!isValid()) return;

        String spId = spidValueLabel.getText();
        String supplierId = supIdField.getText();
        String brand = brandField.getText();
        String name = nameField.getText();
        String amount = amountField.getText();
        String quantity = quantityField.getText();


        InventoryDto inventoryDto = new InventoryDto(
                spId,
                supplierId,
                brand,
                name,
                amount,
                quantity
        );


        try {
            boolean isSave = inventoryBO.saveSparepart(inventoryDto);
            if (isSave) {
                loadNextId();
                loadTableData();

                supIdField.setText("");
                brandField.setText("");
                nameField.setText("");
                amountField.setText("");
                quantityField.setText("");

                new Alert(
                        Alert.AlertType.INFORMATION, "spare part saved successfully..!"
                ).show();
            } else {
                new Alert(
                        Alert.AlertType.ERROR, "Fail to save spare part..!"
                ).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(
                    Alert.AlertType.ERROR, "Fail to save spare part..!"
            ).show();
        }

    }

    private void resetFields() {
        supIdField.clear();
        brandField.clear();
        nameField.clear();
        amountField.clear();
        quantityField.clear();
    }




    public void btnDeleteOnAction(ActionEvent event) {
        String spId = spidValueLabel.getText();

        try {
            boolean isDeleted = inventoryBO.deleteSparepart(spId);
            if (isDeleted) {
                loadNextId();
                loadTableData();
                resetFields();
                new Alert(Alert.AlertType.INFORMATION, "Spare part deleted successfully..!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Delete failed..!").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while deleting..!").show();
        }
    }


    @FXML
    void btnReportOnAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/report/lowQuantitySparepart.jrxml")

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
        resetFields();
    }



    public void btnUpdateOnAction(ActionEvent event) {

        if (!isValid()) return;

        String spId = spidValueLabel.getText();
        String supplierId = supIdField.getText();
        String brand = brandField.getText();
        String name = nameField.getText();
        String amount = amountField.getText();
        String quantity = quantityField.getText();

        InventoryDto inventoryDto = new InventoryDto(spId, supplierId, brand, name, amount, quantity);

        try {
            boolean isUpdated = inventoryBO.updateSparepart(inventoryDto);
            if (isUpdated) {
                loadTableData();
                new Alert(Alert.AlertType.INFORMATION, "Spare part updated successfully..!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Update failed..!").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while updating..!").show();
        }
    }

    @FXML
    void btnSparePartOnAction(ActionEvent event) {
       navigateTo("/view/SparePart.fxml");
    }


    public void btnSupplierOnAction(ActionEvent event) {
         navigateTo("/view/SupplierPage.fxml");
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

