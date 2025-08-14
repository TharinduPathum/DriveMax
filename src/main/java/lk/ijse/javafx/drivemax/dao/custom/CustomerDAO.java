package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.CrudDAO;
import lk.ijse.javafx.drivemax.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CustomerDAO extends CrudDAO<Customer> {

    String getCustomerEmailById(String id) throws SQLException;

    boolean existsCustomerByPhoneNumber(String phoneNumber) throws SQLException;

    Optional<String> getLastCustomerId() throws SQLException;



}
