package lk.ijse.javafx.drivemax.dao.custom.impl;

import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.QueryDAO;
import lk.ijse.javafx.drivemax.entity.Sparepart;
import lk.ijse.javafx.drivemax.db.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class QueryDAOImpl implements QueryDAO {


    @Override
    public boolean saveSparePartUsageAndUpdateInventory(Sparepart sparepart) throws SQLException {
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

            // 2. Reduce inventory count
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
