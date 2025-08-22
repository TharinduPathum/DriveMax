package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.InvoiceBO;
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
import lk.ijse.javafx.drivemax.dto.InvoiceDto;
import lk.ijse.javafx.drivemax.entity.Customer;
import lk.ijse.javafx.drivemax.entity.Invoice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvoiceBOImpl implements InvoiceBO {

    private final InvoiceDAO invoiceDAO = DAOFactory.getInstance().getDAO(DAOTypes.INVOICE);
    private final PaymentDAO paymentDAO = DAOFactory.getInstance().getDAO(DAOTypes.PAYMENT);



    @Override
    public boolean saveInvoice(InvoiceDto invoiceDto) throws DuplicateException, SQLException {
        Optional<Invoice> optionalInvoice = invoiceDAO.findById(invoiceDto.getInvId());
        if (optionalInvoice.isPresent()) {
            throw new DuplicateException("Duplicate invoice id");
        }

        Invoice invoice = EntityDTOConverter.convert(invoiceDto, Invoice.class);

        return invoiceDAO.save(invoice);
    }

    @Override
    public ArrayList<InvoiceDto> getAllInvoices() throws SQLException {
        List<Invoice> invoices = invoiceDAO.getAll();
        ArrayList<InvoiceDto> invoiceDtos = new ArrayList<>();
        for (Invoice invoice : invoices) {
            invoiceDtos.add(EntityDTOConverter.convert(invoice, InvoiceDto.class));
        }
        return invoiceDtos;
    }

    @Override
    public boolean deleteInvoice(String invoiceId) throws InUseException, SQLException {
        Optional<Invoice> optionalInvoice = invoiceDAO.findById(invoiceId);
        if (optionalInvoice.isEmpty()) {
            throw new NotFoundException("Invoice not found..!");
        }

        if (paymentDAO.existsInvoicesByPaymentId(invoiceId)) {
            throw new InUseException("invoices has already been payed..!");
        }

        try {
            return invoiceDAO.delete(invoiceId);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getNextId() throws SQLException {
        Optional<String> optionalId = invoiceDAO.getLastInvoiceId();

        if (optionalId.isEmpty()) {
            return "I001"; // No customers found
        }

        String lastId = optionalId.get();
        String lastIdNumberString = lastId.substring(1);
        int lastIdNumber = Integer.parseInt(lastIdNumberString);
        int nextIdNumber = lastIdNumber + 1;
        return String.format("I%03d", nextIdNumber);
    }

    @Override
    public boolean updateInvoice(InvoiceDto invoiceDto) throws NotFoundException, SQLException {
        Optional<Invoice> optionalInvoice = invoiceDAO.findById(invoiceDto.getInvId());
        if (optionalInvoice.isEmpty()) {
            throw new NotFoundException("Invoice not found");
        }

        Invoice invoice = EntityDTOConverter.convert(invoiceDto, Invoice.class);

        invoiceDAO.update(invoice);
        return true;
    }
}
