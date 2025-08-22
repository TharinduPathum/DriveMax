package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.RecordBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.bo.util.EntityDTOConverter;
import lk.ijse.javafx.drivemax.dao.DAOFactory;
import lk.ijse.javafx.drivemax.dao.DAOTypes;
import lk.ijse.javafx.drivemax.dao.custom.RecordDAO;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.dto.RecordDto;
import lk.ijse.javafx.drivemax.entity.Customer;
import lk.ijse.javafx.drivemax.entity.Record;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecordBOImpl implements RecordBO {

    private final RecordDAO recordDAO = DAOFactory.getInstance().getDAO(DAOTypes.RECORD);

    @Override
    public List<RecordDto> getAllRecords() throws SQLException {
        List<Record> records = recordDAO.getAll();
        List<RecordDto> recordDtos = new ArrayList<>();
        for (Record record : records) {
            recordDtos.add(EntityDTOConverter.convert(record, RecordDto.class));
        }
        return recordDtos;
    }

    @Override
    public String getNextId() throws SQLException {
        Optional<String> optionalId = recordDAO.getLastRecordId();

        if (optionalId.isEmpty()) {
            return "REC001"; // No customers found
        }

        String lastId = optionalId.get();
        String lastIdNumberString = lastId.substring(1);
        int lastIdNumber = Integer.parseInt(lastIdNumberString);
        int nextIdNumber = lastIdNumber + 1;
        return String.format("REC%03d", nextIdNumber);
    }

    @Override
    public boolean saveRecord(RecordDto recordDto) throws DuplicateException, SQLException {
        Optional<Record> optionalRecord = recordDAO.findById(recordDto.getRecId());
        if (optionalRecord.isPresent()) {
            throw new DuplicateException("Duplicate record id");
        }

        Record record = EntityDTOConverter.convert(recordDto, Record.class);

        return recordDAO.save(record);
    }

    @Override
    public boolean updateRecord(RecordDto recordDto) throws NotFoundException, SQLException {
        Optional<Record> optionalRecord = recordDAO.findById(recordDto.getRecId());
        if (optionalRecord.isEmpty()) {
            throw new NotFoundException("Record not found");
        }

        Record record = EntityDTOConverter.convert(recordDto, Record.class);

        recordDAO.update(record);
        return true;
    }

    @Override
    public boolean deleteRecord(String recordId) throws InUseException, SQLException {
        Optional<Record> optionalRecord = recordDAO.findById(recordId);
        if (optionalRecord.isEmpty()) {
            throw new NotFoundException("Record not found..!");
        }

        try {
            return recordDAO.delete(recordId);
        } catch (Exception e) {
            return false;
        }
    }
}
