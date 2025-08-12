package lk.ijse.javafx.drivemax.dao.custom.impl;

import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.RecordDAO;
import lk.ijse.javafx.drivemax.dto.RecordDto;
import lk.ijse.javafx.drivemax.entity.Record;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecordDAOImpl implements RecordDAO {
    @Override
    public List<Record> getAll() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("select * from record");

        List<Record> list = new ArrayList<>();
        while (resultSet.next()) {
            Record record = new Record(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );

            list.add(record);
        }
        return list;
    }

    @Override
    public String getNextId() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT rec_id FROM record ORDER BY rec_id DESC LIMIT 1");
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

    @Override
    public boolean save(Record record) throws SQLException {
        return SQLUtil.execute(
                "insert into record values(?,?,?,?)",
                record.getRecId(),
                record.getVehicleId(),
                record.getDescription(),
                record.getDate()
        );
    }

    @Override
    public boolean update(Record record) throws SQLException {
        return SQLUtil.execute(
                "UPDATE record SET v_id = ?, description = ?, date = ?, WHERE rec_id = ?",

                record.getVehicleId(),
                record.getDescription(),
                record.getDate(),
                record.getRecId()

        );
    }

    @Override
    public boolean delete(String recordId) throws SQLException {
        String sql = "DELETE FROM record WHERE rec_id = ?";
        return SQLUtil.execute(sql, recordId);
    }

    @Override
    public List<String> getAllIds() throws SQLException {
        return List.of();
    }

    @Override
    public Optional<Record> findById(String id) throws SQLException {
        return Optional.empty();
    }
}
