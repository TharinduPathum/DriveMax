package lk.ijse.javafx.drivemax.model;

import lk.ijse.javafx.drivemax.dto.AttendanceDto;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AttendanceModel {

    public boolean saveAttendance(AttendanceDto dto) throws SQLException {
        return CrudUtil.execute(
                "INSERT INTO attendance (e_id, date, status) VALUES (?, ?, ?)",
                dto.getEmpId(),
                dto.getDate(),
                dto.getStatus()
        );
    }

    public ArrayList<AttendanceDto> getAllAttendance() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM attendance");

        ArrayList<AttendanceDto> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new AttendanceDto(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3)
            ));
        }
        return list;
    }

    public boolean deleteAttendance(String empId, String date) throws SQLException {
        return CrudUtil.execute(
                "DELETE FROM attendance WHERE e_id = ? AND date = ?",
                empId,
                date
        );
    }

    public boolean updateAttendance(AttendanceDto dto) throws SQLException {
        return CrudUtil.execute(
                "UPDATE attendance SET status = ? WHERE e_id = ? AND date = ?",
                dto.getStatus(),
                dto.getEmpId(),
                dto.getDate()
        );
    }
}
