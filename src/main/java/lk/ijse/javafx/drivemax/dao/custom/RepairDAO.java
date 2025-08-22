package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.CrudDAO;
import lk.ijse.javafx.drivemax.entity.Repair;

import java.sql.SQLException;
import java.util.Optional;

public interface RepairDAO extends CrudDAO<Repair> {
    boolean existRepairsByEmployeeId(String id) throws SQLException;

    Optional<String> getLastRepairId() throws SQLException;
}
