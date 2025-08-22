package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.CrudDAO;
import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.entity.Payment;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface PaymentDAO extends CrudDAO<Payment> {

    boolean existsInvoicesByPaymentId(String paymentId) throws SQLException;



}
