package lk.ijse.javafx.drivemax.bo.custom;

import lk.ijse.javafx.drivemax.bo.SuperBO;
import lk.ijse.javafx.drivemax.dto.EmployeeDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface EmployeeBO extends SuperBO {

     ArrayList<EmployeeDto> getAllEmployee() throws SQLException;

    ArrayList<String> getAllEmployeeIds() throws SQLException;

    String getEmployeeEmailById(String empId) throws SQLException;

     String getEmployeeName(String empId) throws SQLException;

    String getNextId() throws SQLException;

    boolean saveEmployee(EmployeeDto employeeDto) throws SQLException;

    boolean updateEmployee(EmployeeDto employeeDto) throws Exception;

    boolean deleteEmployee(String empId) throws SQLException;
}
