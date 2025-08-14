package lk.ijse.javafx.drivemax.bo.custom;

import lk.ijse.javafx.drivemax.bo.SuperBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.InvoiceDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface InvoiceBO extends SuperBO {

    boolean saveInvoice(InvoiceDto invoiceDto) throws DuplicateException, SQLException;

    ArrayList<InvoiceDto> getAllInvoices() throws SQLException;

    boolean deleteInvoice(String invoiceId) throws InUseException, SQLException;

    String getNextId() throws SQLException;

    boolean updateInvoice(InvoiceDto invoiceDto) throws NotFoundException, SQLException;
}
