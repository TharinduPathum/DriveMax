package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.CrudDAO;
import lk.ijse.javafx.drivemax.entity.Supplier;

import java.sql.SQLException;
import java.util.Optional;

public interface SupplierDAO extends CrudDAO<Supplier> {

    public String getSupplierEmailById(String supId) throws SQLException;

    Optional<String> getLastSupplierId() throws SQLException;
}
