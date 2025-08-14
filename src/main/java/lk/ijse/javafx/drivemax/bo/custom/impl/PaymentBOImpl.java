package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.PaymentBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.PaymentDto;

import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentBOImpl implements PaymentBO {
    @Override
    public ArrayList<PaymentDto> getAllPayments() throws SQLException {
        return null;
    }

    @Override
    public String getNextId() throws SQLException {
        return "";
    }

    @Override
    public boolean savePayment(PaymentDto paymentDto) throws DuplicateException, SQLException {
        return false;
    }

    @Override
    public boolean updatePayment(PaymentDto paymentDto) throws NotFoundException, SQLException {
        return false;
    }

    @Override
    public boolean deletePayment(String paymentId) throws InUseException, SQLException {
        return false;
    }
}
