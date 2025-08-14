package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.VehicleBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.VehicleDto;

import java.sql.SQLException;
import java.util.ArrayList;

public class VehicleBOImpl implements VehicleBO {
    @Override
    public ArrayList<VehicleDto> getAllVehicle() throws SQLException {
        return null;
    }

    @Override
    public String getNextId() throws SQLException {
        return "";
    }

    @Override
    public boolean saveVehicle(VehicleDto vehicleDto) throws DuplicateException, SQLException {
        return false;
    }

    @Override
    public boolean updateVehicle(VehicleDto vehicledto) throws NotFoundException, SQLException {
        return false;
    }

    @Override
    public boolean deleteVehicle(String vehicleId) throws InUseException, SQLException {
        return false;
    }
}
