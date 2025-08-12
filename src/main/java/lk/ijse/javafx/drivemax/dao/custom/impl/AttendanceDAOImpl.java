package lk.ijse.javafx.drivemax.dao.custom.impl;

import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.AttendanceDAO;
import lk.ijse.javafx.drivemax.dto.AttendanceDto;
import lk.ijse.javafx.drivemax.entity.Attendance;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AttendanceDAOImpl implements AttendanceDAO {
    @Override
    public List<Attendance> getAll() throws SQLException {
        ResultSet rs = SQLUtil.execute("SELECT * FROM attendance");

        List<Attendance> list = new ArrayList<>();
        while (rs.next()) {
            Attendance attendance = new Attendance(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3)
            );
          list.add(attendance);
        }
        return list;
    }

    @Override
    public String getNextId() throws SQLException {
        return "";
    }

    @Override
    public boolean save(Attendance attendance) throws SQLException {
        return SQLUtil.execute(
                "INSERT INTO attendance (e_id, date, status) VALUES (?, ?, ?)",
                attendance.getId(),
                attendance.getDate(),
                attendance.getStatus()
        );
    }

    @Override
    public boolean update(Attendance attendance) throws SQLException {
        return SQLUtil.execute(
                "UPDATE attendance SET status = ? WHERE e_id = ? AND date = ?",
                attendance.getStatus(),
                attendance.getId(),
                attendance.getDate()
        );
    }

    @Override
    public boolean delete(String date) throws SQLException {
        return SQLUtil.execute(
                "DELETE FROM attendance WHERE e_id = ? AND date = ?",
                date

        );
    }

    @Override
    public List<String> getAllIds() throws SQLException {
        return List.of();
    }

    @Override
    public Optional<Attendance> findById(String id) throws SQLException {
        return Optional.empty();
    }


}
