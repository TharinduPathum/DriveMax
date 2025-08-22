package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.SalaryBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.bo.util.EntityDTOConverter;
import lk.ijse.javafx.drivemax.dao.DAOFactory;
import lk.ijse.javafx.drivemax.dao.DAOTypes;
import lk.ijse.javafx.drivemax.dao.custom.SalaryDAO;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.dto.SalaryDto;
import lk.ijse.javafx.drivemax.entity.Customer;
import lk.ijse.javafx.drivemax.entity.Salary;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalaryBOImpl implements SalaryBO {

    private final SalaryDAO salaryDAO= DAOFactory.getInstance().getDAO(DAOTypes.SALARY);

    @Override
    public ArrayList<SalaryDto> getAllSalary() throws SQLException {
        List<Salary> salaries = salaryDAO.getAll();
        ArrayList<SalaryDto> salaryDtos = new ArrayList<>();
        for (Salary salary : salaries) {
            salaryDtos.add(EntityDTOConverter.convert(salary, SalaryDto.class));
        }
        return salaryDtos;
    }

    @Override
    public String getNextId() throws SQLException {
        Optional<String> optionalId = salaryDAO.getLastCustomerId();

        if (optionalId.isEmpty()) {
            return "S001"; // No customers found
        }

        String lastId = optionalId.get();
        String lastIdNumberString = lastId.substring(1);
        int lastIdNumber = Integer.parseInt(lastIdNumberString);
        int nextIdNumber = lastIdNumber + 1;
        return String.format("S%03d", nextIdNumber);
    }

    @Override
    public boolean saveSalary(SalaryDto salaryDto) throws DuplicateException, SQLException {
        Optional<Salary> optionalSalary = salaryDAO.findById(salaryDto.getId());
        if (optionalSalary.isPresent()) {
            throw new DuplicateException("Duplicate salary id");
        }

        Salary salary = EntityDTOConverter.convert(salaryDto, Salary.class);

        return salaryDAO.save(salary);
    }

    @Override
    public boolean updateSalary(SalaryDto salaryDto) throws NotFoundException, SQLException {
        Optional<Salary> optionalSalary = salaryDAO.findById(salaryDto.getId());
        if (optionalSalary.isEmpty()) {
            throw new NotFoundException("Salary not found");
        }

        Salary salary = EntityDTOConverter.convert(salaryDto, Salary.class);

        salaryDAO.update(salary);
        return true;
    }

    @Override
    public boolean deleteSalary(String salaryId) throws InUseException, SQLException {
        Optional<Salary> optionalSalary = salaryDAO.findById(salaryId);
        if (optionalSalary.isEmpty()) {
            throw new NotFoundException("Salary not found..!");
        }

        try {
            return salaryDAO.delete(salaryId);
        } catch (Exception e) {
            return false;
        }
    }
}
