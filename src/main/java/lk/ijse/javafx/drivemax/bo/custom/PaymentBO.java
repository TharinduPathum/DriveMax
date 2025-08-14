package lk.ijse.javafx.drivemax.bo.custom;

import lk.ijse.javafx.drivemax.bo.SuperBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.PaymentDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PaymentBO extends SuperBO {

    ArrayList<PaymentDto> getAllPayments() throws SQLException;

    String getNextId() throws SQLException;

    boolean savePayment(PaymentDto paymentDto) throws DuplicateException, SQLException;

    boolean updatePayment(PaymentDto paymentDto) throws NotFoundException, SQLException;

    boolean deletePayment(String paymentId) throws InUseException, SQLException;





}
