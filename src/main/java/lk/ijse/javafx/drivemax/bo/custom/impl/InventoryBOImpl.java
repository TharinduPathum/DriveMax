package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.InventoryBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.bo.util.EntityDTOConverter;
import lk.ijse.javafx.drivemax.dao.DAOFactory;
import lk.ijse.javafx.drivemax.dao.DAOTypes;
import lk.ijse.javafx.drivemax.dao.custom.CustomerDAO;
import lk.ijse.javafx.drivemax.dao.custom.InventoryDAO;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.dto.InventoryDto;
import lk.ijse.javafx.drivemax.entity.Customer;
import lk.ijse.javafx.drivemax.entity.Employee;
import lk.ijse.javafx.drivemax.entity.Inventory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryBOImpl implements InventoryBO {

    private final InventoryDAO inventoryDAO = DAOFactory.getInstance().getDAO(DAOTypes.INVENTORY);


    @Override
    public List<InventoryDto> getAllSparePart() throws SQLException {
        List<Inventory> inventories = inventoryDAO.getAll();
        ArrayList<InventoryDto> inventoryDtos = new ArrayList<>();
        for (Inventory inventory : inventories) {
            inventoryDtos.add(EntityDTOConverter.convert(inventory, InventoryDto.class));
        }
        return inventoryDtos;
    }

    @Override
    public String getNextId() throws SQLException {
        Optional<String> optionalId = inventoryDAO.getLastInventoryId();

        if (optionalId.isEmpty()) {
            return "SP001";
        }

        String lastId = optionalId.get();
        String lastIdNumberString = lastId.substring(1);
        int lastIdNumber = Integer.parseInt(lastIdNumberString);
        int nextIdNumber = lastIdNumber + 1;
        return String.format("SP%03d", nextIdNumber);
    }


    @Override
    public boolean saveSparepart(InventoryDto inventoryDto) throws DuplicateException, SQLException {
        Optional<Inventory> optionalInventory = inventoryDAO.findById(inventoryDto.getSparePartId());
        if (optionalInventory.isPresent()) {
            throw new DuplicateException("Duplicate sparepart id");
        }

        Inventory inventory = EntityDTOConverter.convert(inventoryDto, Inventory.class);

        return inventoryDAO.save(inventory);
    }

    @Override
    public boolean updateSparepart(InventoryDto inventoryDto) throws NotFoundException, SQLException {
        Optional<Inventory> optionalEmployee = inventoryDAO.findById(inventoryDto.getSparePartId());
        if (optionalEmployee.isEmpty()) {
            throw new NotFoundException("sparepart not found");
        }

        Inventory inventory = EntityDTOConverter.convert(inventoryDto, Inventory.class);

        inventoryDAO.update(inventory);
        return true;
    }

    @Override
    public boolean deleteSparepart(String id) throws InUseException, SQLException {
        Optional<Inventory> optionalInventory = inventoryDAO.findById(id);
        if (optionalInventory.isEmpty()) {
            throw new NotFoundException("sparepart not found..!");
        }

        try {
            return inventoryDAO.delete(id);
        } catch (Exception e) {
            return false;
        }
    }
}
