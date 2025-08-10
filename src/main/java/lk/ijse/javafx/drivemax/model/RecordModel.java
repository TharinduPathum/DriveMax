package lk.ijse.javafx.drivemax.model;

import lk.ijse.javafx.drivemax.db.DBConnection;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.dto.RecordDto;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class RecordModel {
    public ArrayList<RecordDto> getAllRecords() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("select * from record");

        ArrayList<RecordDto> list = new ArrayList<>();
        while (resultSet.next()) {
            RecordDto recordDto = new RecordDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );

            list.add(recordDto);
        }
        return list;
    }

    public String getNextId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT rec_id FROM record ORDER BY rec_id DESC LIMIT 1");
        String prefix = "REC";

        if (resultSet.next()) {
            String lastId = resultSet.getString(1); // e.g., "SPU012"

            // Corrected: Start from index 3 (length of "SPU")
            String lastIdNumberString = lastId.substring(prefix.length());

            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;

            // Format the next ID: SPU + zero-padded number (e.g., SPU013)
            String nextIdString = String.format("%s%03d", prefix, nextIdNumber);
            return nextIdString;
        }

        // If there are no IDs yet
        return prefix + "001";
    }


    public boolean saveRecord(RecordDto recordDto) throws SQLException {

        return CrudUtil.execute(
                "insert into record values(?,?,?,?)",
                recordDto.getRecordId(),
                recordDto.getVehicleId(),
                recordDto.getDescription(),
                recordDto.getDate()
        );
    }

    public boolean updateRecord(RecordDto recordDto) throws SQLException {

        return CrudUtil.execute(
                "UPDATE record SET v_id = ?, description = ?, date = ?, WHERE rec_id = ?",

                recordDto.getVehicleId(),
                recordDto.getDescription(),
                recordDto.getDate(),
                recordDto.getRecordId()

        );
    }

    public boolean deleteRecord(String recordId) throws SQLException {
        String sql = "DELETE FROM record WHERE rec_id = ?";
        return CrudUtil.execute(sql, recordId);
    }


}