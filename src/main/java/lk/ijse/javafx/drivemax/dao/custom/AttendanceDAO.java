package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.CrudDAO;
import lk.ijse.javafx.drivemax.entity.Attendance;

import java.sql.SQLException;

public interface AttendanceDAO extends CrudDAO<Attendance> {


    boolean EmployeeStatusByDate(String date) throws SQLException;
}
