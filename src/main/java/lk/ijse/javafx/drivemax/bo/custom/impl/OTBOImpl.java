package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.OTBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.bo.util.EntityDTOConverter;
import lk.ijse.javafx.drivemax.dao.DAOFactory;
import lk.ijse.javafx.drivemax.dao.DAOTypes;
import lk.ijse.javafx.drivemax.dao.custom.InventoryDAO;
import lk.ijse.javafx.drivemax.dao.custom.OTDAO;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.dto.OTDto;
import lk.ijse.javafx.drivemax.entity.Customer;
import lk.ijse.javafx.drivemax.entity.OT;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OTBOImpl implements OTBO {

    private final OTDAO otdao = DAOFactory.getInstance().getDAO(DAOTypes.OT);

    @Override
    public boolean saveOT(OTDto dto) throws DuplicateException, SQLException {
        Optional<OT> optionalOT = otdao.findById(dto.getEmpId());
        if (optionalOT.isPresent()) {
            throw new DuplicateException("Duplicate OT record id");
        }

        OT ot = EntityDTOConverter.convert(dto, OT.class);

        return otdao.save(ot);
    }

    @Override
    public boolean updateOT(OTDto dto) throws NotFoundException, SQLException {
        Optional<OT> optionalOT = otdao.findById(dto.getEmpId());
        if (optionalOT.isEmpty()) {
            throw new NotFoundException("OT record not found..!");
        }

        try {
            return otdao.delete(dto.getEmpId());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteOT(String empId, String date) throws InUseException, SQLException {
        Optional<OT> optionalOT = otdao.findById(empId);
        if (optionalOT.isEmpty()) {
            throw new NotFoundException("OT record not found..!");
        }

        if (otdao.existsOTsByEmployeeId(empId)){
            throw new InUseException("Employee has OT records");
        }

        try {
            return otdao.delete(empId);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<OTDto> getAllOT() throws SQLException {
        List<OT> ots = otdao.getAll();
        List<OTDto> otDtos = new ArrayList<>();
        for (OT ot : ots) {
            otDtos.add(EntityDTOConverter.convert(ot, OTDto.class));
        }
        return otDtos;
    }
}
