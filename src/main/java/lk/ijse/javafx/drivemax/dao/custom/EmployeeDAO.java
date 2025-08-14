package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.CrudDAO;
import lk.ijse.javafx.drivemax.entity.Employee;

import java.sql.SQLException;
import java.util.Optional;

public interface EmployeeDAO extends CrudDAO<Employee> {

    String getEmployeeEmailById(String empId) throws SQLException;

    String getEmployeeName(String empId) throws SQLException;

    Optional<String> getLastEmployeeId() throws SQLException;

    boolean existsEmployeeByPhoneNumber(String phoneNumber) throws SQLException;
}
