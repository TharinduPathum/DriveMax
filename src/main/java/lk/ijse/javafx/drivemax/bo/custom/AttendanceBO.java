package lk.ijse.javafx.drivemax.bo.custom;

import lk.ijse.javafx.drivemax.bo.SuperBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.AttendanceDto;

import java.sql.SQLException;
import java.util.List;

public interface AttendanceBO extends SuperBO {
    List<AttendanceDto> getAllAttendance() throws SQLException;

    boolean saveAttendance(AttendanceDto attendanceDto) throws DuplicateException, Exception;

    boolean deleteAttendance(String id,String date) throws InUseException, Exception;

    boolean updateAttendance(AttendanceDto dto) throws NotFoundException, Exception;
}
