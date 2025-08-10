package lk.ijse.javafx.drivemax.model;

import lk.ijse.javafx.drivemax.db.DBConnection;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.dto.RepairDto;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class RepairModel {
    public ArrayList<RepairDto> getAllRepairs() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("select * from repair");

        ArrayList<RepairDto> list = new ArrayList<>();
        while (resultSet.next()) {
            RepairDto repairDto = new RepairDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)

            );

            list.add(repairDto);
        }
        return list;
    }

    public String getNextId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT rep_id FROM repair ORDER BY rep_id DESC LIMIT 1");
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


    public boolean saveRepair(RepairDto repairDto) throws SQLException {

        return CrudUtil.execute(
                "insert into repair values (?,?,?,?,?,?)",
                repairDto.getRepairId(),
                repairDto.getVehicleId(),
                repairDto.getEmployeeId(),
                repairDto.getWork(),
                repairDto.getCost(),
                repairDto.getDate()
        );
    }

    public boolean updateRepair(RepairDto repairdto) throws SQLException {

        return CrudUtil.execute(
                "UPDATE repair SET v_id = ?, e_id = ?, work = ?, total_cost =?, Start_date = ? WHERE rep_id = ?",

                repairdto.getVehicleId(),
                repairdto.getEmployeeId(),
                repairdto.getWork(),
                repairdto.getCost(),
                repairdto.getDate(),
                repairdto.getRepairId()



        );
    }

    public boolean deleteRepair(String repairId) throws SQLException {
        String sql = "DELETE FROM repair WHERE rep_id = ?";
        return CrudUtil.execute(sql, repairId);
    }


}