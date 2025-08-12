package lk.ijse.javafx.drivemax.dao.custom;

import lk.ijse.javafx.drivemax.dao.CrudDAO;
import lk.ijse.javafx.drivemax.entity.Vehicle;

import java.sql.SQLException;

public interface VehicleDAO extends CrudDAO<Vehicle> {
    boolean existVehiclesByCustomerId(String id) throws SQLException;
}
