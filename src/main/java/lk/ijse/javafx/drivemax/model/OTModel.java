package lk.ijse.javafx.drivemax.model;

import lk.ijse.javafx.drivemax.dto.OTDto;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OTModel {

    public boolean saveOT(OTDto dto) throws SQLException {
        String sql = "INSERT INTO overtimework (e_id, date, hour_count) VALUES (?, ?, ?)";
        return CrudUtil.execute(sql,
                dto.getEmpId(),
                dto.getDate(),
                dto.getHours());
    }

    public boolean updateOT(OTDto dto) throws SQLException {
        String sql = "UPDATE overtimework SET hour_count = ? WHERE e_id = ? AND date = ?";
        return CrudUtil.execute(sql,
                dto.getHours(),
                dto.getEmpId(),
                dto.getDate());
    }

    public boolean deleteOT(String empId, String date) throws SQLException {
        String sql = "DELETE FROM overtimework WHERE e_id = ? AND date = ?";
        return CrudUtil.execute(sql, empId, date);
    }

    public ArrayList<OTDto> getAllOT() throws SQLException {
        ArrayList<OTDto> list = new ArrayList<>();
        String sql = "SELECT * FROM overtimework";
        ResultSet resultSet = CrudUtil.execute(sql);

        while (resultSet.next()) {
            list.add(new OTDto(
                    resultSet.getString("e_id"),
                    resultSet.getString("date"),
                    resultSet.getString("hour_count")
            ));
        }
        return list;
    }
}
