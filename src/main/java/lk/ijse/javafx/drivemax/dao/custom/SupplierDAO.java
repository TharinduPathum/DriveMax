package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.CrudDAO;
import lk.ijse.javafx.drivemax.entity.Supplier;

import java.sql.SQLException;

public interface SupplierDAO extends CrudDAO<Supplier> {

    public String getSupplierEmailById(String supId) throws SQLException;
}
