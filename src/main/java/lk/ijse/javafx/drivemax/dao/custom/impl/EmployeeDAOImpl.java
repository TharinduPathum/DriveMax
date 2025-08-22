package lk.ijse.javafx.drivemax.dao.custom.impl;

import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.EmployeeDAO;
import lk.ijse.javafx.drivemax.dto.EmployeeDto;
import lk.ijse.javafx.drivemax.entity.Customer;
import lk.ijse.javafx.drivemax.entity.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDAOImpl implements EmployeeDAO {
    @Override
    public List<Employee> getAll() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("select * from employee");

        List<Employee> list = new ArrayList<>();
        while (resultSet.next()) {
            Employee employee = new Employee(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)

            );

            list.add(employee);
        }
        return list;
    }

    @Override
    public String getEmployeeEmailById(String empId) throws SQLException {
        String sql = "SELECT email FROM employee WHERE e_id = ?";
        ResultSet rs = SQLUtil.execute(sql, empId);

        if (rs.next()) {
            return rs.getString("email");
        }

        return null;
    }

    @Override
    public String getEmployeeName(String empId) throws SQLException {
        ResultSet rs = SQLUtil.execute("SELECT name FROM employee WHERE e_id = ?", empId);
        if (rs.next()) {
            return rs.getString("name");
        }
        return null;
    }

    @Override
    public Optional<String> getLastEmployeeId() throws SQLException {
        String sql = "SELECT e_id FROM employee ORDER BY e_id DESC LIMIT 1";
        ResultSet rs = SQLUtil.execute(sql);

        if (rs.next()) {
            return Optional.of(rs.getString("e_id"));
        }
        return Optional.empty();
    }

    @Override
    public boolean existsEmployeeByPhoneNumber(String phoneNumber) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM employee WHERE phone = ?", phoneNumber);
//        if (resultSet.next()){
//            return true;
//        }
//        return false;

        return resultSet.next();
    }

    @Override
    public String getNextId() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("select e_id from employee order by e_id desc limit 1");
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

    @Override
    public boolean save(Employee employee) throws SQLException {
        return SQLUtil.execute(
                "insert into employee (e_id, name, speciality, address, email, phone) values (?,?,?,?,?,?)",
                employee.getId(),
                employee.getName(),
                employee.getSpeciality(),
                employee.getAddress(),
                employee.getEmail(),
                employee.getPhone()
        );
    }

    @Override
    public boolean update(Employee employee) throws SQLException {
        return SQLUtil.execute(
                "UPDATE employee SET name = ?, speciality = ?, address = ?, email = ?, phone = ? WHERE e_id = ?",

                employee.getName(),
                employee.getSpeciality(),
                employee.getAddress(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getId()
        );
    }

    @Override
    public boolean delete(String empId) throws SQLException {
        String sql = "DELETE FROM employee WHERE e_id = ?";
        return SQLUtil.execute(sql,empId);
    }

    @Override
    public List<String> getAllIds() throws SQLException {
        ArrayList<String> empIdList = new ArrayList<>();

        ResultSet resultSet = SQLUtil.execute("SELECT e_id FROM employee");

        while (resultSet.next()) {
            empIdList.add(resultSet.getString("e_id"));
        }

        return empIdList;
    }

    @Override
    public Optional<Employee> findById(String id) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM employee WHERE e_id = ?", id);
        if (resultSet.next()) {
            return Optional.of(new Employee(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            ));
        }
        return Optional.empty();
    }
}
