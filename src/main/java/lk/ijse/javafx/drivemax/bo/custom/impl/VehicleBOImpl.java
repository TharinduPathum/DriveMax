package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.VehicleBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.bo.util.EntityDTOConverter;
import lk.ijse.javafx.drivemax.dao.DAOFactory;
import lk.ijse.javafx.drivemax.dao.DAOTypes;
import lk.ijse.javafx.drivemax.dao.custom.VehicleDAO;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.dto.VehicleDto;
import lk.ijse.javafx.drivemax.entity.Customer;
import lk.ijse.javafx.drivemax.entity.Vehicle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleBOImpl implements VehicleBO {

    private final VehicleDAO vehicleDAO = DAOFactory.getInstance().getDAO(DAOTypes.VEHICLE);

    @Override
    public ArrayList<VehicleDto> getAllVehicle() throws SQLException {
        List<Vehicle> vehicles = vehicleDAO.getAll();
        ArrayList<VehicleDto> vehicleDtos = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            vehicleDtos.add(EntityDTOConverter.convert(vehicle, VehicleDto.class));
        }
        return vehicleDtos;
    }

    @Override
    public String getNextId() throws SQLException {
        Optional<String> optionalId = vehicleDAO.getLastVehicleId();

        if (optionalId.isEmpty()) {
            return "V001"; // No customers found
        }

        String lastId = optionalId.get();
        String lastIdNumberString = lastId.substring(1);
        int lastIdNumber = Integer.parseInt(lastIdNumberString);
        int nextIdNumber = lastIdNumber + 1;
        return String.format("V%03d", nextIdNumber);
    }

    @Override
    public boolean saveVehicle(VehicleDto vehicleDto) throws DuplicateException, SQLException {
        Optional<Vehicle> optionalVehicle = vehicleDAO.findById(vehicleDto.getVehId());
        if (optionalVehicle.isPresent()) {
            throw new DuplicateException("Duplicate vehicle id");
        }

        Vehicle vehicle = EntityDTOConverter.convert(vehicleDto, Vehicle.class);

        return vehicleDAO.save(vehicle);
    }

    @Override
    public boolean updateVehicle(VehicleDto vehicledto) throws NotFoundException, SQLException {
        Optional<Vehicle> optionalVehicle = vehicleDAO.findById(vehicledto.getVehId());
        if (optionalVehicle.isEmpty()) {
            throw new NotFoundException("Vehicle not found");
        }

        Vehicle vehicle = EntityDTOConverter.convert(vehicledto, Vehicle.class);

        vehicleDAO.update(vehicle);
        return true;
    }

    @Override
    public boolean deleteVehicle(String vehicleId) throws InUseException, SQLException {
        Optional<Vehicle> optionalVehicle = vehicleDAO.findById(vehicleId);
        if (optionalVehicle.isEmpty()) {
            throw new NotFoundException("Vehicle not found..!");
        }

        if (vehicleDAO.existVehiclesByCustomerId(vehicleId)) {
            throw new InUseException("Customer has vehicles");
        }

        try {
            return vehicleDAO.delete(vehicleId);
        } catch (Exception e) {
            return false;
        }
    }
}
