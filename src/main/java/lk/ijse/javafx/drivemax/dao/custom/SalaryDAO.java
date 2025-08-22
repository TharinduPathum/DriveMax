package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.CrudDAO;
import lk.ijse.javafx.drivemax.entity.Salary;

import java.sql.SQLException;
import java.util.Optional;

public interface SalaryDAO extends CrudDAO<Salary> {
    Optional<String> getLastCustomerId() throws SQLException;
}
