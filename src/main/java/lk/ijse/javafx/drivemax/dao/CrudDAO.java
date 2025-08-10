package lk.ijse.javafx.drivemax.dao;

import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T> extends SuperDAO {
    List<T> getAll() throws SQLException;

    String getNextId() throws SQLException;

    boolean save(T t) throws SQLException;

    boolean update(T t) throws SQLException;

    boolean delete(String id) throws SQLException;

    List<String> getAllIds() throws SQLException;
}
