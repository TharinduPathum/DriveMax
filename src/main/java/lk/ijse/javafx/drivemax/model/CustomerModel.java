package lk.ijse.javafx.drivemax.model;

import lk.ijse.javafx.drivemax.db.DBConnection;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class CustomerModel {
    public ArrayList<CustomerDto> getAllCustomer() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("select * from customer");

        ArrayList<CustomerDto> list = new ArrayList<>();
        while (resultSet.next()) {
            CustomerDto customerDto = new CustomerDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );

            list.add(customerDto);
        }
        return list;
    }

    public String getNextId() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("select c_id from customer order by c_id desc limit 1");
        char tableChar = 'C';
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

    public ArrayList<String> getAllCustomerIds() throws SQLException {
        String sql = "SELECT c_id FROM customer";

        ResultSet rs = CrudUtil.execute(sql);
        ArrayList<String> idList = new ArrayList<>();

        while (rs.next()) {
            idList.add(rs.getString("c_id"));
        }
        return idList;
    }

    public String getCustomerEmailById(String custId) throws SQLException {
        String sql = "SELECT email FROM customer WHERE c_id = ?";
        ResultSet rs = CrudUtil.execute(sql, custId);

        if (rs.next()) {
            return rs.getString("email");
        }
        return null;
    }

    public boolean saveCustomer(CustomerDto customerDto) throws SQLException {

        return CrudUtil.execute(
                "insert into customer values (?,?,?,?,?)",
                customerDto.getCustomerId(),
                customerDto.getName(),
                customerDto.getAddress(),
                customerDto.getEmail(),
                customerDto.getPhone()
        );
    }

    public boolean updateCustomer(CustomerDto customerdto) throws SQLException {

        return CrudUtil.execute(
         "UPDATE customer SET name = ?, address = ?, email = ?, phone = ? WHERE c_id = ?",

                customerdto.getName(),
                customerdto.getAddress(),
                customerdto.getEmail(),
                customerdto.getPhone(),
                customerdto.getCustomerId()



        );
    }

    public boolean deleteCustomer(String customerId) throws SQLException {
        String sql = "DELETE FROM customer WHERE c_id = ?";
        return CrudUtil.execute(sql, customerId);
    }


}