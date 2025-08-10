package lk.ijse.javafx.drivemax.model;

import lk.ijse.javafx.drivemax.db.DBConnection;
import lk.ijse.javafx.drivemax.dto.PaymentDto;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class PaymentModel {
    public ArrayList<PaymentDto> getAllPayments() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("select * from payment");

        ArrayList<PaymentDto> list = new ArrayList<>();
        while (resultSet.next()) {
            PaymentDto paymentDto = new PaymentDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );

            list.add(paymentDto);
        }
        return list;
    }

    public String getNextId() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("select p_id from payment order by p_id desc limit 1");
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

    public boolean savePayment(PaymentDto paymentDto) throws SQLException {

        return CrudUtil.execute(
                "insert into payment values (?,?,?,?)",
                paymentDto.getPaymentId(),
                paymentDto.getCustomerId(),
                paymentDto.getAmount(),
                paymentDto.getDate()
        );
    }

    public boolean updatePayment(PaymentDto paymentDto) throws SQLException {

        return CrudUtil.execute(
                "UPDATE payment SET c_id = ?, amount = ?, date = ? WHERE p_id = ?",

                paymentDto.getCustomerId(),
                paymentDto.getAmount(),
                paymentDto.getDate(),
                paymentDto.getPaymentId()



        );
    }

    public boolean deletePayment(String paymentId) throws SQLException {
        String sql = "DELETE FROM payment WHERE p_id = ?";
        return CrudUtil.execute(sql, paymentId);
    }


}