package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.CrudDAO;
import lk.ijse.javafx.drivemax.dto.SparepartDto;
import lk.ijse.javafx.drivemax.entity.Sparepart;

import java.sql.SQLException;
import java.util.Optional;

public interface SparepartDAO extends CrudDAO<Sparepart> {


    Optional<String> getLastSparepartId() throws SQLException;
}
