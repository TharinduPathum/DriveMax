package lk.ijse.javafx.drivemax.dao.custom.impl;

import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.VehicleDAO;
import lk.ijse.javafx.drivemax.dto.VehicleDto;
import lk.ijse.javafx.drivemax.entity.Customer;
import lk.ijse.javafx.drivemax.entity.Vehicle;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleDAOImpl implements VehicleDAO {
    @Override
    public List<Vehicle> getAll() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("select * from vehicle");

        List<Vehicle> list = new ArrayList<>();
        while (resultSet.next()) {
            Vehicle vehicle = new Vehicle(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );

            list.add(vehicle);
        }
        return list;
    }

    @Override
    public String getNextId() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("select v_id from vehicle order by v_id desc limit 1");
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

    @Override
    public boolean save(Vehicle vehicle) throws SQLException {
        return SQLUtil.execute(
                "insert into vehicle values (?,?,?,?,?)",
                vehicle.getVehId(),
                vehicle.getCusId(),
                vehicle.getType(),
                vehicle.getBrand(),
                vehicle.getRegNo()
        );
    }

    @Override
    public boolean update(Vehicle vehicle) throws SQLException {
        return SQLUtil.execute(
                "UPDATE vehicle SET c_id = ?, type = ?, brand = ?, registration_no = ? WHERE v_id = ?",

                vehicle.getCusId(),
                vehicle.getType(),
                vehicle.getBrand(),
                vehicle.getRegNo(),
                vehicle.getVehId()

        );
    }

    @Override
    public boolean delete(String vehicleId) throws SQLException {
        String sql = "DELETE FROM vehicle WHERE v_id = ?";
        return SQLUtil.execute(sql, vehicleId);
    }

    @Override
    public List<String> getAllIds() throws SQLException {
        return List.of();
    }

    @Override
    public Optional<Vehicle> findById(String vehicleId) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM vehicle WHERE v_id = ?", vehicleId);
        if (resultSet.next()) {
            return Optional.of(new Vehicle(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return Optional.empty();
    }

    @Override
    public boolean existVehiclesByCustomerId(String vehicleId) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM vehicle WHERE v_id = ?",vehicleId );
        return resultSet.next();
    }
}
