package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.CrudDAO;
import lk.ijse.javafx.drivemax.entity.Inventory;

import java.sql.SQLException;
import java.util.Optional;

public interface InventoryDAO extends CrudDAO<Inventory> {

    Optional<String> getLastInventoryId() throws SQLException;
}
