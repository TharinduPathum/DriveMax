package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.AttendanceBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.bo.util.EntityDTOConverter;
import lk.ijse.javafx.drivemax.dao.DAOFactory;
import lk.ijse.javafx.drivemax.dao.DAOTypes;
import lk.ijse.javafx.drivemax.dao.custom.AttendanceDAO;
import lk.ijse.javafx.drivemax.dao.custom.EmployeeDAO;
import lk.ijse.javafx.drivemax.dto.AttendanceDto;
import lk.ijse.javafx.drivemax.entity.Attendance;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceBOImpl implements AttendanceBO {

    private final AttendanceDAO attendanceDAO = DAOFactory.getInstance().getDAO(DAOTypes.ATTENDANCE);
    private final EmployeeDAO employeeDAO = DAOFactory.getInstance().getDAO(DAOTypes.EMPLOYEE);
    private final EntityDTOConverter converter = new EntityDTOConverter();


    @Override
    public List<AttendanceDto> getAllAttendance() throws SQLException {
        return getAllAttendance();
    }


    @Override
    public boolean saveAttendance(AttendanceDto attendanceDto) throws DuplicateException, Exception {
        return false;
    }

    @Override
    public boolean deleteAttendance(String id) throws InUseException, Exception {
        return false;
    }

    @Override
    public void updateAttendance(AttendanceDto attendanceDto) throws NotFoundException, Exception {

    }
}
