package lk.ijse.javafx.drivemax.dao.custom.impl;

import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.PaymentDAO;
import lk.ijse.javafx.drivemax.dto.PaymentDto;
import lk.ijse.javafx.drivemax.entity.Payment;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {
    @Override
    public List<Payment> getAll() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("select * from payment");

        List<Payment> list = new ArrayList<>();
        while (resultSet.next()) {
            Payment payment = new Payment(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );

            list.add(payment);
        }
        return list;
    }

    @Override
    public String getNextId() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("select p_id from payment order by p_id desc limit 1");
        char tableChar = 'P';
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNUmberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNUmberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableChar + "%03d", nextIdNumber);
            return nextIdString;
        }
        return tableChar + "001";
    }

    @Override
    public boolean save(Payment payment) throws SQLException {
        return SQLUtil.execute(
                "insert into payment values (?,?,?,?)",
                payment.getPayId(),
                payment.getCustomerId(),
                payment.getAmount(),
                payment.getDate()
        );
    }

    @Override
    public boolean update(Payment payment) throws SQLException {
        return SQLUtil.execute(
                "UPDATE payment SET c_id = ?, amount = ?, date = ? WHERE p_id = ?",

                payment.getCustomerId(),
                payment.getAmount(),
                payment.getDate(),
                payment.getPayId()

        );
    }

    @Override
    public boolean delete(String paymentId) throws SQLException {
        String sql = "DELETE FROM payment WHERE p_id = ?";
        return SQLUtil.execute(sql, paymentId);
    }

    @Override
    public List<String> getAllIds() throws SQLException {
        return List.of();
    }
}
