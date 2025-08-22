package lk.ijse.javafx.drivemax.bo.custom;

import lk.ijse.javafx.drivemax.bo.SuperBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.RecordDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface RecordBO extends SuperBO {

    List<RecordDto> getAllRecords() throws SQLException;

    String getNextId() throws SQLException;

    boolean saveRecord(RecordDto recordDto) throws DuplicateException, SQLException;

    boolean updateRecord(RecordDto recordDto) throws NotFoundException, SQLException;

    boolean deleteRecord(String recordId) throws InUseException, SQLException;


}
