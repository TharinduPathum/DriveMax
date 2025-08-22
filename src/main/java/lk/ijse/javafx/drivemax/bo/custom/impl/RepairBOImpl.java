package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.RepairBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.bo.util.EntityDTOConverter;
import lk.ijse.javafx.drivemax.dao.DAOFactory;
import lk.ijse.javafx.drivemax.dao.DAOTypes;
import lk.ijse.javafx.drivemax.dao.custom.CustomerDAO;
import lk.ijse.javafx.drivemax.dao.custom.QueryDAO;
import lk.ijse.javafx.drivemax.dao.custom.RepairDAO;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.dto.RepairDto;
import lk.ijse.javafx.drivemax.entity.Customer;
import lk.ijse.javafx.drivemax.entity.Repair;
import lk.ijse.javafx.drivemax.entity.Sparepart;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepairBOImpl implements RepairBO {

    private final RepairDAO repairDAO = DAOFactory.getInstance().getDAO(DAOTypes.REPAIR);
    private final QueryDAO queryDAO = DAOFactory.getInstance().getDAO(DAOTypes.QUERY);


    @Override
    public List<RepairDto> getAllRepairs() throws SQLException {
        List<Repair> repairs = repairDAO.getAll();
        List<RepairDto> repairDtos = new ArrayList<>();
        for (Repair repair : repairs) {
            repairDtos.add(EntityDTOConverter.convert(repair, RepairDto.class));
        }
        return repairDtos;
    }

    @Override
    public String getNextId() throws SQLException {
        Optional<String> optionalId = repairDAO.getLastRepairId();

        if (optionalId.isEmpty()) {
            return "REP001"; // No customers found
        }

        String lastId = optionalId.get();
        String lastIdNumberString = lastId.substring(1);
        int lastIdNumber = Integer.parseInt(lastIdNumberString);
        int nextIdNumber = lastIdNumber + 1;
        return String.format("REP%03d", nextIdNumber);
    }

    @Override
    public boolean saveRepair(RepairDto repairDto) throws DuplicateException, SQLException {
        Optional<Repair> optionalRepair = repairDAO.findById(repairDto.getRepId());
        if (optionalRepair.isPresent()) {
            throw new DuplicateException("Duplicate repair id");
        }

        Repair repair = EntityDTOConverter.convert(repairDto, Repair.class);

        return repairDAO.save(repair);
    }

    @Override
    public boolean updateRepair(RepairDto repairdto) throws NotFoundException, SQLException {
        Optional<Repair> optionalRepair = repairDAO.findById(repairdto.getRepId());
        if (optionalRepair.isEmpty()) {
            throw new NotFoundException("Repair not found");
        }

        Repair repair = EntityDTOConverter.convert(repairdto, Repair.class);

        repairDAO.update(repair);
        return true;
    }

    @Override
    public boolean deleteRepair(String repairId) throws InUseException, SQLException {
        Optional<Repair> optionalRepair = repairDAO.findById(repairId);
        if (optionalRepair.isEmpty()) {
            throw new NotFoundException("Repair not found..!");
        }

       try {
            return repairDAO.delete(repairId);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean useSparePart(Sparepart sparepart) throws SQLException {
        return queryDAO.saveSparePartUsageAndUpdateInventory(sparepart);

    }
}
