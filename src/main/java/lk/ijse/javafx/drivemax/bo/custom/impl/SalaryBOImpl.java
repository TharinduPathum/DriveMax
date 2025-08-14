package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.SalaryBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.SalaryDto;

import java.sql.SQLException;
import java.util.ArrayList;

public class SalaryBOImpl implements SalaryBO {
    @Override
    public ArrayList<SalaryDto> getAllSalary() throws SQLException {
        return null;
    }

    @Override
    public String getNextId() throws SQLException {
        return "";
    }

    @Override
    public boolean saveSalary(SalaryDto salaryDto) throws DuplicateException, SQLException {
        return false;
    }

    @Override
    public boolean updateSalary(SalaryDto salaryDto) throws NotFoundException, SQLException {
        return false;
    }

    @Override
    public boolean deleteSalary(String salaryId) throws InUseException, SQLException {
        return false;
    }
}
