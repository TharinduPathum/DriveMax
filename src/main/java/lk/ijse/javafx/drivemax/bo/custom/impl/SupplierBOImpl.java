package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.BOFactory;
import lk.ijse.javafx.drivemax.bo.BOTypes;
import lk.ijse.javafx.drivemax.bo.custom.SupplierBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.bo.util.EntityDTOConverter;
import lk.ijse.javafx.drivemax.dao.DAOFactory;
import lk.ijse.javafx.drivemax.dao.DAOTypes;
import lk.ijse.javafx.drivemax.dao.custom.CustomerDAO;
import lk.ijse.javafx.drivemax.dao.custom.SupplierDAO;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.dto.SupplierDto;
import lk.ijse.javafx.drivemax.entity.Customer;
import lk.ijse.javafx.drivemax.entity.Supplier;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplierBOImpl implements SupplierBO {

    private final SupplierDAO supplierDAO = DAOFactory.getInstance().getDAO(DAOTypes.SUPPLIER);


    @Override
    public List<SupplierDto> getAllSupplier() throws SQLException {
        List<Supplier> suppliers = supplierDAO.getAll();
        List<SupplierDto> supplierDtos = new ArrayList<>();
        for (Supplier supplier : suppliers) {
            supplierDtos.add(EntityDTOConverter.convert(supplier, SupplierDto.class));
        }
        return supplierDtos;
    }

    @Override
    public String getNextId() throws SQLException {
        Optional<String> optionalId = supplierDAO.getLastSupplierId();

        if (optionalId.isEmpty()) {
            return "SUP001";
        }

        String lastId = optionalId.get();
        String lastIdNumberString = lastId.substring(1);
        int lastIdNumber = Integer.parseInt(lastIdNumberString);
        int nextIdNumber = lastIdNumber + 1;
        return String.format("SUP%03d", nextIdNumber);
    }

    @Override
    public ArrayList<String> getAllSupplierIds() throws SQLException {
        ArrayList<String> idList = (ArrayList<String>) supplierDAO.getAllIds();

        if (idList.isEmpty()) {
            throw new NotFoundException("No suppliers found..!");
        }

        return idList;
    }

    @Override
    public boolean saveSupplier(SupplierDto supplierDto) throws DuplicateException, SQLException {
        Optional<Supplier> optionalSupplier = supplierDAO.findById(supplierDto.getId());
        if (optionalSupplier.isPresent()) {
            throw new DuplicateException("Duplicate supplier id");
        }

       Supplier supplier = EntityDTOConverter.convert(supplierDto, Supplier.class);

        return supplierDAO.save(supplier);
    }

    @Override
    public boolean updateSupplier(SupplierDto supplierDto) throws NotFoundException, SQLException {
        Optional<Supplier> optionalSupplier = supplierDAO.findById(supplierDto.getId());
        if (optionalSupplier.isEmpty()) {
            throw new NotFoundException("Supplier not found");
        }

        Supplier supplier = EntityDTOConverter.convert(supplierDto, Supplier.class);

        supplierDAO.update(supplier);
        return true;
    }

    @Override
    public boolean deleteSupplier(String supplierId) throws InUseException, SQLException {
        Optional<Supplier> optionalSupplier = supplierDAO.findById(supplierId);
        if (optionalSupplier.isEmpty()) {
            throw new NotFoundException("Supplier not found..!");
        }

        try {
            return supplierDAO.delete(supplierId);
        } catch (Exception e) {
            return false;
        }
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
