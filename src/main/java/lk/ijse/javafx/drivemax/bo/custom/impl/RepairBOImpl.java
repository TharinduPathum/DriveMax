package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.RepairBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.RepairDto;

import java.sql.SQLException;
import java.util.ArrayList;

public class RepairBOImpl implements RepairBO {
    @Override
    public ArrayList<RepairDto> getAllRepairs() throws SQLException {
        return null;
    }

    @Override
    public String getNextId() throws SQLException {
        return "";
    }

    @Override
    public boolean saveRepair(RepairDto repairDto) throws DuplicateException, SQLException {
        return false;
    }

    @Override
    public boolean updateRepair(RepairDto repairdto) throws NotFoundException, SQLException {
        return false;
    }

    @Override
    public boolean deleteRepair(String repairId) throws InUseException, SQLException {
        return false;
    }
}
