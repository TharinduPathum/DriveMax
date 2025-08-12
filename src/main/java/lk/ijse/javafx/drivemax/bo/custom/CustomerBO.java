package lk.ijse.javafx.drivemax.bo.custom;

import lk.ijse.javafx.drivemax.bo.SuperBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.CustomerDto;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBO extends SuperBO {
    List<CustomerDto> getAllCustomer() throws SQLException;

    boolean saveCustomer(CustomerDto customerDto) throws DuplicateException, Exception;

    boolean deleteCustomer(String id) throws InUseException, Exception;

    void updateCustomer(CustomerDto customerDto) throws NotFoundException, Exception;
}
