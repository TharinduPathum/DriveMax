package lk.ijse.javafx.drivemax.bo.custom;

import lk.ijse.javafx.drivemax.bo.SuperBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.SalaryDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SalaryBO extends SuperBO {

    ArrayList<SalaryDto> getAllSalary() throws SQLException;

    String getNextId() throws SQLException;

    boolean saveSalary(SalaryDto salaryDto) throws DuplicateException, SQLException;

    boolean updateSalary(SalaryDto salaryDto) throws NotFoundException, SQLException;

    boolean deleteSalary(String salaryId) throws InUseException, SQLException;


}
