package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.SparepartBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.SparepartDto;

import java.sql.SQLException;
import java.util.ArrayList;

public class SparepartBOImpl implements SparepartBO {
    @Override
    public ArrayList<SparepartDto> getAllSparePartUsages() throws SQLException {
        return null;
    }

    @Override
    public String getNextId() throws SQLException {
        return "";
    }

    @Override
    public boolean saveSparePartUsage(SparepartDto sparepartDto) throws DuplicateException, SQLException {
        return false;
    }

    @Override
    public boolean updateSparePartUsage(SparepartDto sparepartDto) throws NotFoundException, SQLException {
        return false;
    }

    @Override
    public boolean deleteSparePartUsage(String sparePartId) throws InUseException, SQLException {
        return false;
    }

    @Override
    public boolean saveSparePartUsageWithInventoryUpdate(SparepartDto sparepartDto) throws SQLException {
        return false;
    }
}
