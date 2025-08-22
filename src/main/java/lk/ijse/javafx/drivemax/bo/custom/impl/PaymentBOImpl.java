package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.PaymentBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.bo.util.EntityDTOConverter;
import lk.ijse.javafx.drivemax.dao.DAOFactory;
import lk.ijse.javafx.drivemax.dao.DAOTypes;
import lk.ijse.javafx.drivemax.dao.custom.CustomerDAO;
import lk.ijse.javafx.drivemax.dao.custom.InvoiceDAO;
import lk.ijse.javafx.drivemax.dao.custom.PaymentDAO;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.dto.PaymentDto;
import lk.ijse.javafx.drivemax.entity.Customer;
import lk.ijse.javafx.drivemax.entity.Payment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentBOImpl implements PaymentBO {

    private final PaymentDAO paymentDAO = DAOFactory.getInstance().getDAO(DAOTypes.PAYMENT);
    private final InvoiceDAO invoiceDAO = DAOFactory.getInstance().getDAO(DAOTypes.INVOICE);


    @Override
    public ArrayList<PaymentDto> getAllPayments() throws SQLException {
        List<Payment> payments = paymentDAO.getAll();
        List<PaymentDto> paymentDtos = new ArrayList<>();
        for (Payment payment : payments) {
            paymentDtos.add(EntityDTOConverter.convert(payment, PaymentDto.class));
        }
        return (ArrayList<PaymentDto>) paymentDtos;
    }

    @Override
    public String getNextId() throws SQLException {
        return "";
    }

    @Override
    public boolean savePayment(PaymentDto paymentDto) throws DuplicateException, SQLException {
        Optional<Payment> optionalPayment = paymentDAO.findById(paymentDto.getCustomerId());
        if (optionalPayment.isPresent()) {
            throw new DuplicateException("Duplicate Payment id");
        }

        Payment payment = EntityDTOConverter.convert(paymentDto, Payment.class);

        return paymentDAO.save(payment);
    }

    @Override
    public boolean updatePayment(PaymentDto paymentDto) throws NotFoundException, SQLException {
        Optional<Payment> optionalPayment = paymentDAO.findById(paymentDto.getPayId());
        if (optionalPayment.isEmpty()) {
            throw new NotFoundException("payment not found");
        }

        Payment payment = EntityDTOConverter.convert(paymentDto, Payment.class);

        paymentDAO.update(payment);
        return true;
    }

    @Override
    public boolean deletePayment(String paymentId) throws InUseException, SQLException {
        Optional<Payment> optionalPayment = paymentDAO.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            throw new NotFoundException("Payment not found..!");
        }

        try {
            return paymentDAO.delete(paymentId);
        } catch (Exception e) {
            return false;
        }
    }
}
