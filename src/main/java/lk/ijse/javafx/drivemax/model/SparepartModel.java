package lk.ijse.javafx.drivemax.model;

import lk.ijse.javafx.drivemax.dto.SparepartDto;
import lk.ijse.javafx.drivemax.util.CrudUtil;
import lk.ijse.javafx.drivemax.db.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SparepartModel {

    public ArrayList<SparepartDto> getAllSparePartUsages() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM sparepart");

        ArrayList<SparepartDto> list = new ArrayList<>();
        while (resultSet.next()) {
            SparepartDto sparepartDto = new SparepartDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            );
            list.add(sparepartDto);
        }
        return list;
    }

    public String getNextId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT sp_id FROM sparepart ORDER BY sp_id DESC LIMIT 1");
        String prefix = "SP";

        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(prefix.length());
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format("%s%03d", prefix, nextIdNumber);
        }

        return prefix + "001";
    }

    public boolean saveSparePartUsage(SparepartDto sparepartDto) throws SQLException {
        return CrudUtil.execute(
                "INSERT INTO sparepart VALUES (?, ?, ?)",
                sparepartDto.getSparePartId(),
                sparepartDto.getRepair(),
                sparepartDto.getDate()
        );
    }

    public boolean updateSparePartUsage(SparepartDto sparepartDto) throws SQLException {
        return CrudUtil.execute(
                "UPDATE sparepart SET rep_id = ?, used_date = ? WHERE sp_id = ?",
                sparepartDto.getRepair(),
                sparepartDto.getDate(),
                sparepartDto.getSparePartId()
        );
    }

    public boolean deleteSparePartUsage(String sparePartId) throws SQLException {
        return CrudUtil.execute(
                "DELETE FROM sparepart WHERE sp_id = ?",
                sparePartId
        );
    }

    public boolean saveSparePartUsageWithInventoryUpdate(SparepartDto dto) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();

        try {
            conn.setAutoCommit(false);

            boolean isInserted = CrudUtil.execute(
                    "INSERT INTO sparepart VALUES (?, ?, ?)",
                    dto.getSparePartId(),
                    dto.getRepair(),
                    dto.getDate()
            );

            if (!isInserted) {
                conn.rollback();
                return false;
            }

            boolean isUpdated = CrudUtil.execute(
                    "UPDATE inventory SET quantity = quantity - 1 WHERE sp_id = ? AND quantity > 0",
                    dto.getSparePartId()
            );

            if (!isUpdated) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
            return false;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
