package lk.ijse.javafx.drivemax.dao.custom.impl;

import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.InvoiceDAO;
import lk.ijse.javafx.drivemax.dto.InvoiceDto;
import lk.ijse.javafx.drivemax.entity.Invoice;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvoiceDAOImpl implements InvoiceDAO {
    @Override
    public List<Invoice> getAll() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("select * from invoice");

        List<Invoice> list = new ArrayList<>();
        while (resultSet.next()) {
            Invoice invoice = new Invoice(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)

            );

            list.add(invoice);
        }
        return list;
    }

    @Override
    public String getNextId() throws SQLException {
        return "";
    }

    @Override
    public boolean save(Invoice invoice) throws SQLException {
        return SQLUtil.execute(
                "insert into invoice values (?,?,?,?,?)",
                invoice.getInvId(),
                invoice.getPaymentId(),
                invoice.getCustomerId(),
                invoice.getDescription(),
                invoice.getDate()
        );
    }

    @Override
    public boolean update(Invoice invoice) throws SQLException {
        return SQLUtil.execute(
                "UPDATE invoice SET p_id = ?, c_id = ?, details = ?, date = ? WHERE i_id = ?",
                invoice.getPaymentId(),
                invoice.getCustomerId(),
                invoice.getDescription(),
                invoice.getDate(),
                invoice.getInvId()
        );
    }

    @Override
    public boolean delete(String invid) throws SQLException {
        return SQLUtil.execute(
                "DELETE FROM invoice WHERE i_id = ?",
                invid
        );
    }

    @Override
    public List<String> getAllIds() throws SQLException {
        return List.of();
    }

    @Override
    public Optional<Invoice> findById(String id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public Optional<String> getLastInvoiceId() throws SQLException {
        String sql = "SELECT i_id FROM invoice ORDER BY i_id DESC LIMIT 1";
        ResultSet rs = SQLUtil.execute(sql);

        if (rs.next()) {
            return Optional.of(rs.getString("i_id"));
        }
        return Optional.empty();
    }


}
