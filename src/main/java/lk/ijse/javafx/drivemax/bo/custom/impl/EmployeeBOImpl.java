package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.EmployeeBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.bo.util.EntityDTOConverter;
import lk.ijse.javafx.drivemax.dao.DAOFactory;
import lk.ijse.javafx.drivemax.dao.DAOTypes;
import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.AttendanceDAO;
import lk.ijse.javafx.drivemax.dao.custom.EmployeeDAO;
import lk.ijse.javafx.drivemax.dao.custom.RepairDAO;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.dto.EmployeeDto;
import lk.ijse.javafx.drivemax.entity.Customer;
import lk.ijse.javafx.drivemax.entity.Employee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeBOImpl implements EmployeeBO {

    private final EmployeeDAO employeeDAO = DAOFactory.getInstance().getDAO(DAOTypes.EMPLOYEE);
    private final RepairDAO repairDAO = DAOFactory.getInstance().getDAO(DAOTypes.REPAIR);



    @Override
    public ArrayList<EmployeeDto> getAllEmployee() throws SQLException {

            List<Employee> employees = employeeDAO.getAll();
            ArrayList<EmployeeDto> employeeDtos = new ArrayList<>();
            for (Employee employee : employees) {
                employeeDtos.add(EntityDTOConverter.convert(employee, EmployeeDto.class));
            }
            return employeeDtos;
    }

    @Override
    public ArrayList<String> getAllEmployeeIds() throws SQLException {
        ArrayList<String> idList = (ArrayList<String>) employeeDAO.getAllIds();

        if (idList.isEmpty()) {
            throw new NotFoundException("No employees found..!");
        }

        return idList;
    }

    @Override
    public String getEmployeeEmailById(String empId) throws SQLException {
        Optional<Employee> optionalEmployee = employeeDAO.findById(empId);

        if (optionalEmployee.isEmpty()) {
            throw new NotFoundException("Employee not found..!");
        }

        return optionalEmployee.get().getEmail();
    }

    @Override
    public String getEmployeeName(String empId) throws SQLException {

            Optional<Employee> optionalEmployee = employeeDAO.findById(empId);

            if (optionalEmployee.isEmpty()) {
                throw new NotFoundException("Employee not found..!");
            }

            return optionalEmployee.get().getName();
        }



    @Override
    public String getNextId() throws SQLException {
        Optional<String> optionalId = employeeDAO.getLastEmployeeId();

        if (optionalId.isEmpty()) {
            return "E001"; // No customers found
        }

        String lastId = optionalId.get();
        String lastIdNumberString = lastId.substring(1);
        int lastIdNumber = Integer.parseInt(lastIdNumberString);
        int nextIdNumber = lastIdNumber + 1;
        return String.format("E%03d", nextIdNumber);
    }

    @Override
    public boolean saveEmployee(EmployeeDto employeeDto) throws DuplicateException, SQLException {
        Optional<Employee> optionalEmployee = employeeDAO.findById(employeeDto.getEmployeeId());
        if (optionalEmployee.isPresent()) {
            throw new DuplicateException("Duplicate employee id");
        }

        if (employeeDAO.existsEmployeeByPhoneNumber(employeeDto.getPhone())) {
            throw new DuplicateException("Duplicate employee phone number");
        }

        Employee employee = EntityDTOConverter.convert(employeeDto, Employee.class);

        return employeeDAO.save(employee);
    }

    @Override
    public boolean updateEmployee(EmployeeDto employeeDto) throws NotFoundException, SQLException {
        Optional<Employee> optionalEmployee = employeeDAO.findById(employeeDto.getEmployeeId());
        if (optionalEmployee.isEmpty()) {
            throw new NotFoundException("Employee not found");
        }

        Employee employee = EntityDTOConverter.convert(employeeDto, Employee.class);

        employeeDAO.update(employee);
        return true;
    }

    @Override
    public boolean deleteEmployee(String id) throws InUseException, SQLException {
        Optional<Employee> optionalEmployee = employeeDAO.findById(id);
        if (optionalEmployee.isEmpty()) {
            throw new NotFoundException("Employee not found..!");
        }

        if (repairDAO.existRepairsByEmployeeId(id)) {
            throw new InUseException("Employee has repairs");
        }

        try {
            return employeeDAO.delete(id);
        } catch (Exception e) {
            return false;
        }
    }


}
