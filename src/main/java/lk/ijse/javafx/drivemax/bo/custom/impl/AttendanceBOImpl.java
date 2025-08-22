package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.AttendanceBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.bo.util.EntityDTOConverter;
import lk.ijse.javafx.drivemax.dao.DAOFactory;
import lk.ijse.javafx.drivemax.dao.DAOTypes;
import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.AttendanceDAO;
import lk.ijse.javafx.drivemax.dao.custom.EmployeeDAO;
import lk.ijse.javafx.drivemax.dto.AttendanceDto;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.entity.Attendance;
import lk.ijse.javafx.drivemax.entity.Customer;
import lk.ijse.javafx.drivemax.util.CrudUtil;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AttendanceBOImpl implements AttendanceBO {

    private final AttendanceDAO attendanceDAO = DAOFactory.getInstance().getDAO(DAOTypes.ATTENDANCE);
    private final EntityDTOConverter converter = new EntityDTOConverter();


    @Override
    public List<AttendanceDto> getAllAttendance() throws SQLException {
        List<Attendance> attendances = attendanceDAO.getAll();
        List<AttendanceDto> attendanceDtos = new ArrayList<>();
        for (Attendance attendance : attendances) {
            attendanceDtos.add(EntityDTOConverter.convert(attendance, AttendanceDto.class));
        }
        return attendanceDtos;
    }


    @Override
    public boolean saveAttendance(AttendanceDto dto) throws DuplicateException, Exception {
        Optional<Attendance> optionalAttendance = attendanceDAO.findById(dto.getId());
        if (optionalAttendance.isPresent()) {
            throw new DuplicateException("Duplicate customer id");
        }

        Attendance attendance = EntityDTOConverter.convert(dto, Attendance.class);

        return attendanceDAO.save(attendance);
    }

    @Override
    public boolean deleteAttendance(String empId ,String date) throws InUseException, Exception {
        Optional<Attendance> optionalAttendance = attendanceDAO.findById(empId);
        if (optionalAttendance.isEmpty()) {
            throw new InUseException("Customer not found..!");
        }

        try {
            return attendanceDAO.delete(empId);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateAttendance(AttendanceDto dto) throws NotFoundException, Exception {
        Optional<Attendance> optionalAttendance = attendanceDAO.findById(dto.getId());
        if (optionalAttendance.isEmpty()) {
            throw new NotFoundException("Employee not attended");
        }

        Attendance attendance = EntityDTOConverter.convert(dto, Attendance.class);

        attendanceDAO.update(attendance);
        return false;
    }
}
