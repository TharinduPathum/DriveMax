package lk.ijse.javafx.drivemax.dao.custom.impl;

import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.RepairDAO;
import lk.ijse.javafx.drivemax.dto.RepairDto;
import lk.ijse.javafx.drivemax.entity.Repair;
import lk.ijse.javafx.drivemax.entity.Supplier;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepairDAOImpl implements RepairDAO {
    @Override
    public List<Repair> getAll() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("select * from repair");

        List<Repair> list = new ArrayList<>();
        while (resultSet.next()) {
            Repair repair = new Repair(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)

            );

            list.add(repair);
        }
        return list;
    }

    @Override
    public String getNextId() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT rep_id FROM repair ORDER BY rep_id DESC LIMIT 1");
        String prefix = "REP";

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
    public boolean save(Repair repair) throws SQLException {
        return SQLUtil.execute(
                "insert into repair values (?,?,?,?,?,?)",
                repair.getRepId(),
                repair.getVehicleId(),
                repair.getEmployeeId(),
                repair.getWork(),
                repair.getCost(),
                repair.getDate()
        );
    }

    @Override
    public boolean update(Repair repair) throws SQLException {
        return SQLUtil.execute(
                "UPDATE repair SET v_id = ?, e_id = ?, work = ?, total_cost =?, Start_date = ? WHERE rep_id = ?",

                repair.getVehicleId(),
                repair.getEmployeeId(),
                repair.getWork(),
                repair.getCost(),
                repair.getDate(),
                repair.getRepId()



        );

    }

    @Override
    public boolean delete(String repairId) throws SQLException {
        String sql = "DELETE FROM repair WHERE rep_id = ?";
        return SQLUtil.execute(sql, repairId);
    }

    @Override
    public List<String> getAllIds() throws SQLException {
        return List.of();
    }

    @Override
    public Optional<Repair> findById(String id) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM repair WHERE rep_id = ?", id);
        if (resultSet.next()) {
            return Optional.of(new Repair(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            ));
        }
        return Optional.empty();
    }

    @Override
    public boolean existRepairsByEmployeeId(String id) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM repair WHERE rep_id = ?",id );
        return resultSet.next();
    }

    @Override
    public Optional<String> getLastRepairId() throws SQLException {
        String sql = "SELECT rep_id FROM repair ORDER BY rep_id DESC LIMIT 1";
        ResultSet rs = SQLUtil.execute(sql);

        if (rs.next()) {
            return Optional.of(rs.getString("rep_id"));
        }
        return Optional.empty();
    }
}
