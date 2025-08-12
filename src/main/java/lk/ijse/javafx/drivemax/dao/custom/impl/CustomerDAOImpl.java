package lk.ijse.javafx.drivemax.dao.custom.impl;

import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.CustomerDAO;
import lk.ijse.javafx.drivemax.entity.Customer;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public List<Customer> getAll() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM customer");

        List<Customer> list = new ArrayList<>();
        while (resultSet.next()) {
            Customer customer = new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            list.add(customer);
        }
        return list;
    }

    @Override
    public String getNextId() throws SQLException {

        ResultSet resultSet = SQLUtil.execute("select c_id from customer order by c_id desc limit 1");
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

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        String sql = "SELECT c_id FROM customer";

        ResultSet rs = SQLUtil.execute(sql);
        ArrayList<String> idList = new ArrayList<>();

        while (rs.next()) {
            idList.add(rs.getString("c_id"));
        }
        return idList;
    }

    @Override
    public Optional<Customer> findById(String id) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM customer WHERE c_id = ?", id);
        if (resultSet.next()) {
            return Optional.of(new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return Optional.empty();
    }

    @Override
    public String getCustomerEmailById(String id) throws SQLException {
        String sql = "SELECT email FROM customer WHERE c_id = ?";
        ResultSet rs = SQLUtil.execute(sql, id);

        if (rs.next()) {
            return rs.getString("email");
        }
        return null;
    }

    @Override
    public boolean existsCustomerByPhoneNumber(String phoneNumber) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM customer WHERE phone = ?", phoneNumber);
//        if (resultSet.next()){
//            return true;
//        }
//        return false;

        return resultSet.next(); //related data exist, can't add a data
    }

    @Override
    public boolean save(Customer customer) throws SQLException {

        return SQLUtil.execute(
                "insert into customer(c_id, name, address, email, phone) values (?, ?, ?, ?, ?)",
                customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getEmail(),
                customer.getPhone()
        );
    }

    @Override
    public boolean update(Customer customer) throws SQLException {

        return SQLUtil.execute(
                "UPDATE customer SET name = ?, address = ?, email = ?, phone = ? WHERE c_id = ?",

                customer.getName(),
                customer.getAddress(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getId()

        );
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM customer WHERE c_id = ?";
        return SQLUtil.execute(sql, id);
    }

}
