package lk.ijse.javafx.drivemax.bo.custom;

import lk.ijse.javafx.drivemax.bo.SuperBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.CustomerDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface CustomerBO extends SuperBO {
    List<CustomerDto> getAllCustomer() throws SQLException;

    boolean saveCustomer(CustomerDto customerDto) throws DuplicateException, Exception;

    boolean deleteCustomer(String id) throws InUseException, Exception;

    boolean updateCustomer(CustomerDto customerDto) throws NotFoundException, Exception;

    String getNextId() throws Exception;

    ArrayList<String> getAllCustomerIds() throws NotFoundException, Exception;

    String getCustomerEmailById(String id) throws NotFoundException, Exception;
}
