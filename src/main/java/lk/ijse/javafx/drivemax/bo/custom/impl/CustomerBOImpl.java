package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.CustomerBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.bo.util.EntityDTOConverter;
import lk.ijse.javafx.drivemax.dao.DAOFactory;
import lk.ijse.javafx.drivemax.dao.DAOTypes;
import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.CustomerDAO;
import lk.ijse.javafx.drivemax.dao.custom.VehicleDAO;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.entity.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerBOImpl implements CustomerBO {

    private final CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOTypes.CUSTOMER);
    private final VehicleDAO vehicleDAO = DAOFactory.getInstance().getDAO(DAOTypes.VEHICLE);

    @Override
    public List<CustomerDto> getAllCustomer() throws SQLException {
        List<Customer> customers = customerDAO.getAll();
        List<CustomerDto> customerDTOS = new ArrayList<>();
        for (Customer customer : customers) {
            customerDTOS.add(EntityDTOConverter.convert(customer, CustomerDto.class));
        }
        return customerDTOS;
    }

    @Override
    public boolean saveCustomer(CustomerDto customerDto) throws DuplicateException, Exception {
        Optional<Customer> optionalCustomer = customerDAO.findById(customerDto.getCustomerId());
        if (optionalCustomer.isPresent()) {
            throw new DuplicateException("Duplicate customer id");
        }

        if (customerDAO.existsCustomerByPhoneNumber(customerDto.getPhone())) {
            throw new DuplicateException("Duplicate customer phone number");
        }

        Customer customer = EntityDTOConverter.convert(customerDto, Customer.class);

        return customerDAO.save(customer);
    }

    @Override
    public boolean deleteCustomer(String id) throws InUseException, Exception {
        Optional<Customer> optionalCustomer = customerDAO.findById(id);
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("Customer not found..!");
        }

        if (vehicleDAO.existVehiclesByCustomerId(id)) {
            throw new InUseException("Customer has vehicles");
        }

        try {
            return customerDAO.delete(id);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) throws NotFoundException, Exception {
        Optional<Customer> optionalCustomer = customerDAO.findById(customerDto.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("Customer not found");
        }

        Customer customer = EntityDTOConverter.convert(customerDto, Customer.class);

        customerDAO.update(customer);
        return true;
    }

    @Override
    public String getNextId() throws Exception {
        Optional<String> optionalId = customerDAO.getLastCustomerId();

        if (optionalId.isEmpty()) {
            return "C001"; // No customers found
        }

        String lastId = optionalId.get();
        String lastIdNumberString = lastId.substring(1);
        int lastIdNumber = Integer.parseInt(lastIdNumberString);
        int nextIdNumber = lastIdNumber + 1;
        return String.format("C%03d", nextIdNumber);
    }

    @Override
    public ArrayList<String> getAllCustomerIds() throws NotFoundException, Exception {
        ArrayList<String> idList = (ArrayList<String>) customerDAO.getAllIds();

        if (idList.isEmpty()) {
            throw new NotFoundException("No customers found..!");
        }

        return idList;
    }

    @Override
    public String getCustomerEmailById(String id) throws NotFoundException, Exception {
        Optional<Customer> optionalCustomer = customerDAO.findById(id);

        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("Customer not found..!");
        }

        return optionalCustomer.get().getEmail();
    }

}


