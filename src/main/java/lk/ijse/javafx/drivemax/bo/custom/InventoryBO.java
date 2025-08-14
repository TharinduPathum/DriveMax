package lk.ijse.javafx.drivemax.bo.custom;

import lk.ijse.javafx.drivemax.bo.SuperBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.InventoryDto;
import lk.ijse.javafx.drivemax.entity.Inventory;

import java.sql.SQLException;
import java.util.List;

public interface InventoryBO extends SuperBO {

    List<InventoryDto> getAllSparePart() throws SQLException;

    String getNextId() throws SQLException;

    boolean saveSparepart(InventoryDto inventoryDto) throws DuplicateException, SQLException;

    boolean updateSparepart(InventoryDto inventoryDto) throws NotFoundException, SQLException;

    boolean deleteSparepart(String id) throws InUseException, SQLException;
}
