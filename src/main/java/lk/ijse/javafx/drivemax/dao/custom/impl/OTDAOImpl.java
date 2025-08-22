package lk.ijse.javafx.drivemax.dao.custom.impl;

import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.OTDAO;
import lk.ijse.javafx.drivemax.entity.Employee;
import lk.ijse.javafx.drivemax.entity.OT;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OTDAOImpl implements OTDAO {
    @Override
    public List<OT> getAll() throws SQLException {
        List<OT> list = new ArrayList<>();
        String sql = "SELECT * FROM overtimework";
        ResultSet resultSet = SQLUtil.execute(sql);

        while (resultSet.next()) {
            OT ot = new OT(
                    resultSet.getString("e_id"),
                    resultSet.getString("date"),
                    resultSet.getString("hour_count")
            );
        }
        return list;
    }

    @Override
    public boolean existsOTsByEmployeeId(String id) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM overtimework WHERE e_id = ?", id);
//        if (resultSet.next()){
//            return true;
//        }
//        return false;

        return resultSet.next();
    }

    @Override
    public boolean save(OT ot) throws SQLException {
        String sql = "INSERT INTO overtimework (e_id, date, hour_count) VALUES (?, ?, ?)";
        return SQLUtil.execute(sql,
                ot.getEmpId(),
                ot.getDate(),
                ot.getHours());
    }

    @Override
    public boolean update(OT ot) throws SQLException {
        String sql = "UPDATE overtimework SET hour_count = ? WHERE e_id = ? AND date = ?";
        return SQLUtil.execute(sql,
                ot.getHours(),
                ot.getEmpId(),
                ot.getDate());
    }

    @Override
    public boolean delete(String empId) throws SQLException {
        String sql = "DELETE FROM overtimework WHERE e_id = ? AND date = ?";
        return SQLUtil.execute(sql, empId);    }

    @Override
    public List<String> getAllIds() throws SQLException {
        return List.of();
    }

    @Override
    public Optional<OT> findById(String id) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM overtimework WHERE e_id = ?", id);
        if (resultSet.next()) {
            return Optional.of(new OT(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            ));
        }
        return Optional.empty();
    }

    @Override
    public String getNextId() throws SQLException {
        return "";
    }
}
