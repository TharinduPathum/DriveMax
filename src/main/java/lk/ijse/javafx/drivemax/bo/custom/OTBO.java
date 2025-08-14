package lk.ijse.javafx.drivemax.bo.custom;

import lk.ijse.javafx.drivemax.bo.SuperBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.OTDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OTBO extends SuperBO {

    boolean saveOT(OTDto dto) throws DuplicateException, SQLException;

    boolean updateOT(OTDto dto) throws NotFoundException, SQLException;

    boolean deleteOT(String empId, String date) throws InUseException, SQLException;

    ArrayList<OTDto> getAllOT() throws SQLException;
}
