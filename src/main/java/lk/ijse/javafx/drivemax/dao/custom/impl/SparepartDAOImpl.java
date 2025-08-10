package lk.ijse.javafx.drivemax.dao.custom.impl;

import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.SparepartDAO;
import lk.ijse.javafx.drivemax.db.DBConnection;
import lk.ijse.javafx.drivemax.dto.SparepartDto;
import lk.ijse.javafx.drivemax.entity.Sparepart;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SparepartDAOImpl implements SparepartDAO {

    @Override
    public List<Sparepart> getAll() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM sparepart");

        List<Sparepart> list = new ArrayList<>();
        while (resultSet.next()) {
            Sparepart sparepart = new Sparepart(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            );
            list.add(sparepart);
        }
        return list;
    }

    @Override
    public String getNextId() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT sp_id FROM sparepart ORDER BY sp_id DESC LIMIT 1");
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

    @Override
    public boolean save(Sparepart sparepart) throws SQLException {
        return SQLUtil.execute(
                "INSERT INTO sparepart VALUES (?, ?, ?)",
                sparepart.getSpId(),
                sparepart.getRepair(),
                sparepart.getDate()
        );
    }

    @Override
    public boolean update(Sparepart sparepart) throws SQLException {
        return SQLUtil.execute(
                "UPDATE sparepart SET rep_id = ?, used_date = ? WHERE sp_id = ?",
                sparepart.getRepair(),
                sparepart.getDate(),
                sparepart.getSpId()
        );
    }

    @Override
    public boolean delete(String sparePartId) throws SQLException {
        return SQLUtil.execute(
                "DELETE FROM sparepart WHERE sp_id = ?",
                sparePartId
        );
    }

    @Override
    public List<String> getAllIds() throws SQLException {
        return List.of();
    }

    @Override
    public boolean saveSparePartUsageWithInventoryUpdate(Sparepart sparepart) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();

        try {
            conn.setAutoCommit(false);

            boolean isInserted = SQLUtil.execute(
                    "INSERT INTO sparepart VALUES (?, ?, ?)",
                    sparepart.getSpId(),
                    sparepart.getRepair(),
                    sparepart.getDate()
            );

            if (!isInserted) {
                conn.rollback();
                return false;
            }

            boolean isUpdated = SQLUtil.execute(
                    "UPDATE inventory SET quantity = quantity - 1 WHERE sp_id = ? AND quantity > 0",
                    sparepart.getSpId()
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
