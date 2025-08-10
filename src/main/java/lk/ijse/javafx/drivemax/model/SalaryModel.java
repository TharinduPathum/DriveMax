package lk.ijse.javafx.drivemax.model;

import lk.ijse.javafx.drivemax.db.DBConnection;
import lk.ijse.javafx.drivemax.dto.SalaryDto;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class SalaryModel {
    public ArrayList<SalaryDto> getAllSalary() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("select * from salary");

        ArrayList<SalaryDto> list = new ArrayList<>();
        while (resultSet.next()) {
            SalaryDto salaryDto = new SalaryDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7)

            );

            list.add(salaryDto);
        }
        return list;
    }

    public String getNextId() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("select s_id from salary order by s_id desc limit 1");
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

    public boolean saveSalary(SalaryDto salaryDto) throws SQLException {

        return CrudUtil.execute(
                "insert into salary values (?,?,?,?,?,?,?)",
                salaryDto.getSalaryId(),
                salaryDto.getEmployeeId(),
                salaryDto.getAttendance(),
                salaryDto.getOtWork(),
                salaryDto.getDsalary(),
                salaryDto.getMsalary(),
                salaryDto.getDate()
        );
    }

    public boolean updateSalary(SalaryDto salaryDto) throws SQLException {

        return CrudUtil.execute(
                "UPDATE salary SET employee_id = ?, attended_day_count = ?, over_time_hours = ?,daily_salary = ?, monthly_salary = ? ,date = ? WHERE s_id = ?",

                salaryDto.getEmployeeId(),
                salaryDto.getAttendance(),
                salaryDto.getOtWork(),
                salaryDto.getDsalary(),
                salaryDto.getMsalary(),
                salaryDto.getDate(),
                salaryDto.getSalaryId()


        );
    }

    public boolean deleteSalary(String salaryId) throws SQLException {
        String sql = "DELETE FROM salary WHERE s_id = ?";
        return CrudUtil.execute(sql, salaryId);
    }


}