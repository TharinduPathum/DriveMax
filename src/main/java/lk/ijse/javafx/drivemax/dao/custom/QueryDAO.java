package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.SuperDAO;
import lk.ijse.javafx.drivemax.entity.Sparepart;

import java.sql.SQLException;

public interface QueryDAO extends SuperDAO {

    boolean saveSparePartUsageAndUpdateInventory(Sparepart sparepart) throws SQLException;

}
