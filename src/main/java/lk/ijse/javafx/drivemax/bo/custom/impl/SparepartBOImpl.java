package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.SparepartBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.bo.util.EntityDTOConverter;
import lk.ijse.javafx.drivemax.dao.DAOFactory;
import lk.ijse.javafx.drivemax.dao.DAOTypes;
import lk.ijse.javafx.drivemax.dao.custom.SparepartDAO;
import lk.ijse.javafx.drivemax.db.DBConnection;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.dto.SparepartDto;
import lk.ijse.javafx.drivemax.entity.Customer;
import lk.ijse.javafx.drivemax.entity.Sparepart;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SparepartBOImpl implements SparepartBO {

    private final SparepartDAO sparepartDAO = DAOFactory.getInstance().getDAO(DAOTypes.SPAREPART);

    @Override
    public ArrayList<SparepartDto> getAllSparePartUsages() throws SQLException {
        List<Sparepart> spareparts = sparepartDAO.getAll();
        ArrayList<SparepartDto> sparepartDtos = new ArrayList<>();
        for (Sparepart sparepart : spareparts) {
            sparepartDtos.add(EntityDTOConverter.convert(sparepart, SparepartDto.class));
        }
        return sparepartDtos;
    }

    @Override
    public String getNextId() throws SQLException {
        Optional<String> optionalId = sparepartDAO.getLastSparepartId();

        if (optionalId.isEmpty()) {
            return "SP001"; // No customers found
        }

        String lastId = optionalId.get();
        String lastIdNumberString = lastId.substring(1);
        int lastIdNumber = Integer.parseInt(lastIdNumberString);
        int nextIdNumber = lastIdNumber + 1;
        return String.format("SP%03d", nextIdNumber);
    }

    @Override
    public boolean saveSparePartUsage(SparepartDto sparepartDto) throws DuplicateException, SQLException {
        Optional<Sparepart> optionalSparepart = sparepartDAO.findById(sparepartDto.getSpId());
        if (optionalSparepart.isPresent()) {
            throw new DuplicateException("Duplicate sparepart usage id");
        }

        Sparepart sparepart = EntityDTOConverter.convert(sparepartDto, Sparepart.class);

        return sparepartDAO.save(sparepart);
    }

    @Override
    public boolean updateSparePartUsage(SparepartDto sparepartDto) throws  SQLException {
        Optional<Sparepart> optionalSparepart = sparepartDAO.findById(sparepartDto.getSpId());
        if (optionalSparepart.isEmpty()) {
            throw new SQLException("Sparepart usage not found");
        }

        Sparepart sparepart = EntityDTOConverter.convert(sparepartDto, Sparepart.class);

        sparepartDAO.update(sparepart);
        return true;
    }

    @Override
    public boolean deleteSparePartUsage(String sparePartId) throws InUseException, SQLException {
        Optional<Sparepart> optionalSparepart = sparepartDAO.findById(sparePartId);
        if (optionalSparepart.isEmpty()) {
            throw new NotFoundException("Sparepart Usage not found..!");
        }

        try {
            return sparepartDAO.delete(sparePartId);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean saveSparePartUsageWithInventoryUpdate(SparepartDto sparepartDto) throws SQLException {
        return sparepartDAO.save(EntityDTOConverter.convert(sparepartDto, Sparepart.class));
    }
}
