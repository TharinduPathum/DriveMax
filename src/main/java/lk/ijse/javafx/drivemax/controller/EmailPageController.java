package lk.ijse.javafx.drivemax.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lk.ijse.javafx.drivemax.model.CustomerModel;
import lk.ijse.javafx.drivemax.model.EmployeeModel;
import lk.ijse.javafx.drivemax.model.SupplierModel;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.Properties;

public class EmailPageController {

    @FXML
    private AnchorPane ancPane;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> idCombo;

    @FXML
    private TextArea messageArea;

    @FXML
    private ComboBox<String> recipientTypeCombo;

    @FXML
    private TextField subjectField;

    @FXML
    private Button btnSendMail;

    @FXML
    private Button btnCansel;

    private final CustomerModel customerModel = new CustomerModel();
    private final EmployeeModel employeeModel = new EmployeeModel();
    private final SupplierModel supplierModel = new SupplierModel();

    @FXML
    public void initialize() {

        recipientTypeCombo.setItems(FXCollections.observableArrayList("Customer", "Employee", "Supplier"));


        recipientTypeCombo.setOnAction(event -> {
            String type = recipientTypeCombo.getValue();
            ObservableList<String> ids = FXCollections.observableArrayList();

            try {
                switch (type) {
                    case "Customer" -> ids.setAll(customerModel.getAllCustomerIds());
                    case "Employee" -> ids.setAll(employeeModel.getAllEmployeeIds());
                    case "Supplier" -> ids.setAll(supplierModel.getAllSupplierIds());
                }
                idCombo.setItems(ids);
                emailField.clear();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Failed to load recipient IDs.");
                e.printStackTrace();
            }
        });

        idCombo.setOnAction(event -> {
            String id = idCombo.getValue();
            String type = recipientTypeCombo.getValue();
            if (id == null || type == null) return;

            try {
                String email = switch (type) {
                    case "Customer" -> customerModel.getCustomerEmailById(id);
                    case "Employee" -> employeeModel.getEmployeeEmailById(id);
                    case "Supplier" -> supplierModel.getSupplierEmailById(id);
                    default -> "";
                };
                emailField.setText(email);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Failed to load email address.");
                e.printStackTrace();
            }
        });

        btnSendMail.setOnAction(event -> sendEmail());
        btnCansel.setOnAction(event -> clearFields());
    }

    private void sendEmail() {
        String to = emailField.getText();
        String subject = subjectField.getText();
        String body = messageArea.getText();

        if (to.isBlank() || subject.isBlank() || body.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "All fields are required!");
            return;
        }


        String from = "ptharindupathum2002@gmail.com";
        String password = "THar(2002)";


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");


        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(from));
            mail.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            mail.setSubject(subject);
            mail.setText(body);

            Transport.send(mail);
            showAlert(Alert.AlertType.INFORMATION, "Email sent successfully!");
            clearFields();
        } catch (MessagingException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to send email!");
        }
    }

    private void clearFields() {
        recipientTypeCombo.getSelectionModel().clearSelection();
        idCombo.getItems().clear();
        idCombo.getSelectionModel().clearSelection();
        emailField.clear();
        subjectField.clear();
        messageArea.clear();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.initOwner(ancPane.getScene().getWindow());
        alert.show();
    }
}
