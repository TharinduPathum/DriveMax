package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.CrudDAO;
import lk.ijse.javafx.drivemax.entity.Invoice;

import java.sql.SQLException;
import java.util.Optional;

public interface InvoiceDAO extends CrudDAO<Invoice> {

    Optional<String> getLastInvoiceId() throws SQLException;

}
