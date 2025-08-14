package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.BOFactory;
import lk.ijse.javafx.drivemax.bo.BOTypes;
import lk.ijse.javafx.drivemax.bo.custom.SupplierBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dao.DAOFactory;
import lk.ijse.javafx.drivemax.dao.DAOTypes;
import lk.ijse.javafx.drivemax.dao.custom.CustomerDAO;
import lk.ijse.javafx.drivemax.dao.custom.SupplierDAO;
import lk.ijse.javafx.drivemax.dto.SupplierDto;
import lk.ijse.javafx.drivemax.entity.Customer;
import lk.ijse.javafx.drivemax.entity.Supplier;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class SupplierBOImpl implements SupplierBO {

    private final SupplierDAO supplierDAO = DAOFactory.getInstance().getDAO(DAOTypes.SUPPLIER);


    @Override
    public ArrayList<SupplierDto> getAllSupplier() throws SQLException {
        return null;
    }

    @Override
    public String getNextId() throws SQLException {
        return "";
    }

    @Override
    public ArrayList<String> getAllSupplierIds() throws SQLException {
        return null;
    }

    @Override
    public boolean saveSupplier(SupplierDto supplierDto) throws DuplicateException, SQLException {
        return false;
    }

    @Override
    public boolean updateSupplier(SupplierDto supplierDto) throws NotFoundException, SQLException {
        return false;
    }

    @Override
    public boolean deleteSupplier(String supplierId) throws InUseException, SQLException {
        return false;
    }

    @Override
    public String getSupplierEmailById(String id) throws NotFoundException, SQLException {
        Optional<Supplier> optionalSupplier = supplierDAO.findById(id);

        if (optionalSupplier.isEmpty()) {
            throw new NotFoundException("Supplier not found..!");
        }

        return optionalSupplier.get().getEmail();
    }
}
