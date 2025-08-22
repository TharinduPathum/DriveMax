package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.CrudDAO;
import lk.ijse.javafx.drivemax.entity.OT;

import java.sql.SQLException;
import java.util.List;

public interface OTDAO extends CrudDAO<OT> {
    List<OT> getAll() throws SQLException;

    boolean existsOTsByEmployeeId(String empId) throws SQLException;
}
