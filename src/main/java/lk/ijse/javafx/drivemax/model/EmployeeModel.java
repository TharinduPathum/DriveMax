package lk.ijse.javafx.drivemax.model;

import lk.ijse.javafx.drivemax.db.DBConnection;
import lk.ijse.javafx.drivemax.dto.EmployeeDto;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class EmployeeModel {
    public ArrayList<EmployeeDto> getAllEmployee() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("select * from employee");

        ArrayList<EmployeeDto> list = new ArrayList<>();
        while (resultSet.next()) {
            EmployeeDto employeeDto = new EmployeeDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)

            );

            list.add(employeeDto);
        }
        return list;
    }

    public ArrayList<String> getAllEmployeeIds() throws SQLException {
        ArrayList<String> empIdList = new ArrayList<>();

        ResultSet resultSet = CrudUtil.execute("SELECT e_id FROM employee");

        while (resultSet.next()) {
            empIdList.add(resultSet.getString("e_id"));
        }

        return empIdList;
    }

    public String getEmployeeEmailById(String empId) throws SQLException {
        String sql = "SELECT email FROM employee WHERE e_id = ?";
        ResultSet rs = CrudUtil.execute(sql, empId);

        if (rs.next()) {
            return rs.getString("email");
        }

        return null;
    }

    public String getEmployeeName(String empId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT name FROM employee WHERE e_id = ?", empId);
        if (rs.next()) {
            return rs.getString("name");
        }
        return null;
    }


    public String getNextId() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("select e_id from employee order by e_id desc limit 1");
        char tableChar = 'E';
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNUmberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNUmberString);
            int nextIdNumber = lastIdNumber + 1; // 5
            String nextIdString = String.format(tableChar + "%03d", nextIdNumber);
            return nextIdString;
        }
        return tableChar + "001";
    }

    public boolean saveEmployee(EmployeeDto employeeDto) throws SQLException {

        return CrudUtil.execute(
                "insert into employee values (?,?,?,?,?,?)",
                employeeDto.getEmployeeId(),
                employeeDto.getName(),
                employeeDto.getSpeciality(),
                employeeDto.getAddress(),
                employeeDto.getEmail(),
                employeeDto.getPhone()
        );
    }

    public boolean updateEmployee(EmployeeDto employeeDto) throws SQLException {
        return CrudUtil.execute(
                "UPDATE employee SET name = ?, speciality = ?, address = ?, email = ?, phone = ? WHERE e_id = ?",

                employeeDto.getName(),
                employeeDto.getSpeciality(),
                employeeDto.getAddress(),
                employeeDto.getEmail(),
                employeeDto.getPhone(),
                employeeDto.getEmployeeId()
        );
    }

    public boolean deleteEmployee(String empId) throws SQLException {
        String sql = "DELETE FROM employee WHERE e_id = ?";
        return CrudUtil.execute(sql, empId);
    }

}