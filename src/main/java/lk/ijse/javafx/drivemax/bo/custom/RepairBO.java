package lk.ijse.javafx.drivemax.bo.custom;

import lk.ijse.javafx.drivemax.bo.SuperBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.RepairDto;
import lk.ijse.javafx.drivemax.entity.Sparepart;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface RepairBO extends SuperBO {

    List<RepairDto> getAllRepairs() throws SQLException;

    String getNextId() throws SQLException;

    boolean saveRepair(RepairDto repairDto) throws DuplicateException, SQLException;

    boolean updateRepair(RepairDto repairdto) throws NotFoundException, SQLException;

    boolean deleteRepair(String repairId) throws InUseException, SQLException;

    boolean useSparePart(Sparepart sparepart) throws SQLException;


}
