package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.CrudDAO;
import lk.ijse.javafx.drivemax.dto.SparepartDto;
import lk.ijse.javafx.drivemax.entity.Sparepart;

import java.sql.SQLException;

public interface SparepartDAO extends CrudDAO<Sparepart> {

    public boolean saveSparePartUsageWithInventoryUpdate(Sparepart sparepart) throws SQLException;
}
