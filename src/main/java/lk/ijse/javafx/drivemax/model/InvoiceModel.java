package lk.ijse.javafx.drivemax.model;


import lk.ijse.javafx.drivemax.dto.InvoiceDto;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InvoiceModel {

    public boolean saveInvoice(InvoiceDto invoiceDto) throws SQLException {
        return CrudUtil.execute(
                "insert into invoice values (?,?,?,?,?)",
                invoiceDto.getInvoiceId(),
                invoiceDto.getPaymentId(),
                invoiceDto.getCustomerId(),
                invoiceDto.getDescription(),
                invoiceDto.getDate()
        );
    }


        public ArrayList<InvoiceDto> getAllInvoices() throws SQLException {

            ResultSet resultSet = CrudUtil.execute("select * from invoice");

            ArrayList<InvoiceDto> list = new ArrayList<>();
            while (resultSet.next()) {
                InvoiceDto invoiceDto = new InvoiceDto(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5)

                );

                list.add(invoiceDto);
            }
            return list;
        }

    public boolean deleteInvoice(String invoiceId) throws SQLException {
        return CrudUtil.execute(
                "DELETE FROM invoice WHERE i_id = ?",
                invoiceId
        );
    }

    public boolean updateInvoice(InvoiceDto invoiceDto) throws SQLException {
        return CrudUtil.execute(
                "UPDATE invoice SET p_id = ?, c_id = ?, details = ?, date = ? WHERE i_id = ?",
                invoiceDto.getPaymentId(),
                invoiceDto.getCustomerId(),
                invoiceDto.getDescription(),
                invoiceDto.getDate(),
                invoiceDto.getInvoiceId()
        );
    }

    public String getNextId() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("select i_id from invoice order by i_id desc limit 1");
        char tableChar = 'I';
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNUmberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNUmberString);
            int nextIdNumber = lastIdNumber + 1; // 5
            String nextIdString = String.format(tableChar + "%03d", nextIdNumber);
            return nextIdString;
        }
        return tableChar + "001";
    }
}
