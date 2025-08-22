package lk.ijse.javafx.drivemax.dao.custom.impl;

import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.SalaryDAO;
import lk.ijse.javafx.drivemax.dto.SalaryDto;
import lk.ijse.javafx.drivemax.entity.Salary;
import lk.ijse.javafx.drivemax.entity.Supplier;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalaryDAOImpl implements SalaryDAO {
    @Override
    public List<Salary> getAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select * from salary");

        List<Salary> list = new ArrayList<>();
        while (resultSet.next()) {
            Salary salary = new Salary(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7)

            );

            list.add(salary);
        }
        return list;
    }

    @Override
    public String getNextId() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("select s_id from salary order by s_id desc limit 1");
        char tableChar = 'S';
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNUmberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNUmberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableChar + "%03d", nextIdNumber);
            return nextIdString;
        }
        return tableChar + "001";
    }

    @Override
    public boolean save(Salary salary) throws SQLException {
        return SQLUtil.execute(
                "insert into salary values (?,?,?,?,?,?,?)",
                salary.getEmpId(),
                salary.getEmpId(),
                salary.getAttend(),
                salary.getOt(),
                salary.getDsalary(),
                salary.getMsalary(),
                salary.getDate()
        );
    }

    @Override
    public boolean update(Salary salary) throws SQLException {
        return SQLUtil.execute(
                "UPDATE salary SET employee_id = ?, attended_day_count = ?, over_time_hours = ?,daily_salary = ?, monthly_salary = ? ,date = ? WHERE s_id = ?",

                salary.getEmpId(),
                salary.getAttend(),
                salary.getOt(),
                salary.getDsalary(),
                salary.getMsalary(),
                salary.getDate(),
                salary.getId()




        );
    }

    @Override
    public boolean delete(String salaryId) throws SQLException {
        String sql = "DELETE FROM salary WHERE s_id = ?";
        return SQLUtil.execute(sql, salaryId);
    }

    @Override
    public List<String> getAllIds() throws SQLException {
        return List.of();
    }

    @Override
    public Optional<Salary> findById(String id) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM salary WHERE s_id = ?", id);
        if (resultSet.next()) {
            return Optional.of(new Salary(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7)
            ));
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getLastCustomerId() throws SQLException {
        String sql = "SELECT s_id FROM salary ORDER BY s_id DESC LIMIT 1";
        ResultSet rs = SQLUtil.execute(sql);

        if (rs.next()) {
            return Optional.of(rs.getString("s_id"));
        }
        return Optional.empty();
    }
}
