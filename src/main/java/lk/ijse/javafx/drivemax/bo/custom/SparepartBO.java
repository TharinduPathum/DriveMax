package lk.ijse.javafx.drivemax.bo.custom;

import lk.ijse.javafx.drivemax.bo.SuperBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.SparepartDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SparepartBO extends SuperBO {

    ArrayList<SparepartDto> getAllSparePartUsages() throws SQLException;

    String getNextId() throws SQLException;

    boolean saveSparePartUsage(SparepartDto sparepartDto) throws DuplicateException, SQLException;

    boolean updateSparePartUsage(SparepartDto sparepartDto) throws NotFoundException, SQLException;

    boolean deleteSparePartUsage(String sparePartId) throws InUseException, SQLException;

    boolean saveSparePartUsageWithInventoryUpdate(SparepartDto sparepartDto) throws SQLException;
}
