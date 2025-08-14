package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.RecordBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.dto.RecordDto;

import java.sql.SQLException;
import java.util.ArrayList;

public class RecordBOImpl implements RecordBO {
    @Override
    public ArrayList<RecordDto> getAllRecords() throws SQLException {
        return null;
    }

    @Override
    public String getNextId() throws SQLException {
        return "";
    }

    @Override
    public boolean saveRecord(RecordDto recordDto) throws DuplicateException, SQLException {
        return false;
    }

    @Override
    public boolean updateRecord(RecordDto recordDto) throws NotFoundException, SQLException {
        return false;
    }

    @Override
    public boolean deleteRecord(String recordId) throws InUseException, SQLException {
        return false;
    }
}
