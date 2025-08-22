package lk.ijse.javafx.drivemax.bo.custom;

import lk.ijse.javafx.drivemax.bo.SuperBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.SupplierDto;
import lk.ijse.javafx.drivemax.entity.Supplier;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface SupplierBO extends SuperBO {

    List<SupplierDto> getAllSupplier() throws SQLException;

    String getNextId() throws SQLException;

    ArrayList<String> getAllSupplierIds() throws SQLException;

    boolean saveSupplier(SupplierDto supplierDto) throws DuplicateException, SQLException;

    boolean updateSupplier(SupplierDto supplierDto) throws NotFoundException, SQLException;

    boolean deleteSupplier(String supplierId) throws InUseException, SQLException;

    String getSupplierEmailById(String id) throws NotFoundException, SQLException;


}

