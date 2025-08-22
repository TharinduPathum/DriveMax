package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.CrudDAO;
import lk.ijse.javafx.drivemax.entity.Record;

import java.sql.SQLException;
import java.util.Optional;

public interface RecordDAO extends CrudDAO<Record> {

    Optional<String> getLastRecordId() throws SQLException;

}
