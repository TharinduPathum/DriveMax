package lk.ijse.javafx.drivemax.model;

import lk.ijse.javafx.drivemax.db.DBConnection;
import lk.ijse.javafx.drivemax.dto.VehicleDto;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VehicleModel {
    public ArrayList<VehicleDto> getAllVehicle() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("select * from vehicle");

        ArrayList<VehicleDto> list = new ArrayList<>();
        while (resultSet.next()) {
            VehicleDto vehicleDto = new VehicleDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );

            list.add(vehicleDto);
        }
        return list;
}

    public String getNextId() throws SQLException {

       ResultSet resultSet = CrudUtil.execute("select v_id from vehicle order by v_id desc limit 1");
        char tableChar = 'V';
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNUmberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNUmberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableChar + "%03d", nextIdNumber);
            return nextIdString;
        }
        return tableChar + "001";
    }

    public boolean saveVehicle(VehicleDto vehicleDto) throws SQLException {

      return CrudUtil.execute(
                "insert into vehicle values (?,?,?,?,?)",
                vehicleDto.getVehicleId(),
                vehicleDto.getCustomerId(),
                vehicleDto.getType(),
                vehicleDto.getBrand(),
                vehicleDto.getRegNo()
        );
    }

    public boolean updateVehicle(VehicleDto vehicledto) throws SQLException {

        return CrudUtil.execute(
                "UPDATE vehicle SET c_id = ?, type = ?, brand = ?, registration_no = ? WHERE v_id = ?",

               vehicledto.getCustomerId(),
               vehicledto.getType(),
               vehicledto.getBrand(),
               vehicledto.getRegNo(),
               vehicledto.getVehicleId()

           );

    }

    public boolean deleteVehicle(String vehicleId) throws SQLException {
        String sql = "DELETE FROM vehicle WHERE v_id = ?";
         return CrudUtil.execute(sql, vehicleId);

    }


}

