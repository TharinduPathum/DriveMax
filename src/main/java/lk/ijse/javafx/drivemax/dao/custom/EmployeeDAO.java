package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.CrudDAO;
import lk.ijse.javafx.drivemax.entity.Employee;

import java.sql.SQLException;

public interface EmployeeDAO extends CrudDAO<Employee> {

    String getEmployeeEmailById(String empId) throws SQLException;

    String getEmployeeName(String empId) throws SQLException;
}
