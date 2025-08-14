package lk.ijse.javafx.drivemax.bo.custom;

import lk.ijse.javafx.drivemax.bo.SuperBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.VehicleDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface VehicleBO extends SuperBO {

    ArrayList<VehicleDto> getAllVehicle() throws SQLException;

    String getNextId() throws SQLException;

    boolean saveVehicle(VehicleDto vehicleDto) throws DuplicateException, SQLException;

    boolean updateVehicle(VehicleDto vehicledto) throws NotFoundException, SQLException;

    boolean deleteVehicle(String vehicleId) throws InUseException, SQLException;
}
