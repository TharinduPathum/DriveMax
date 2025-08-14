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

public class OTBOImpl implements OTBO {

    private final OTDAO otdao = DAOFactory.getInstance().getDAO(DAOTypes.OT);

    @Override
    public boolean saveOT(OTDto dto) throws DuplicateException, SQLException {
        return false;
    }

    @Override
    public boolean updateOT(OTDto dto) throws NotFoundException, SQLException {
        return false;
    }

    @Override
    public boolean deleteOT(String empId, String date) throws InUseException, SQLException {
        return false;
    }

    @Override
    public ArrayList<OTDto> getAllOT() throws SQLException {
        List<OT> ots = otdao.getAll();
        List<OTDto> otDtos = new ArrayList<>();
        for (OT ot : ots) {
            otDtos.add(EntityDTOConverter.convert(ot, OTDto.class));
        }
        return (ArrayList<OTDto>) otDtos;
    }
}
