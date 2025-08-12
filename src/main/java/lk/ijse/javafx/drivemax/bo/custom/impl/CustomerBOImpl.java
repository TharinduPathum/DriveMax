package lk.ijse.javafx.drivemax.bo.custom.impl;

import lk.ijse.javafx.drivemax.bo.custom.CustomerBO;
import lk.ijse.javafx.drivemax.bo.exception.DuplicateException;
import lk.ijse.javafx.drivemax.bo.exception.InUseException;
import lk.ijse.javafx.drivemax.bo.exception.NotFoundException;
import lk.ijse.javafx.drivemax.bo.util.EntityDTOConverter;
import lk.ijse.javafx.drivemax.dao.DAOFactory;
import lk.ijse.javafx.drivemax.dao.DAOTypes;
import lk.ijse.javafx.drivemax.dao.custom.CustomerDAO;
import lk.ijse.javafx.drivemax.dao.custom.VehicleDAO;
import lk.ijse.javafx.drivemax.dto.CustomerDto;
import lk.ijse.javafx.drivemax.entity.Customer;

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
    public void updateCustomer(CustomerDto customerDto) throws NotFoundException, Exception {
        Optional<Customer> optionalCustomer = customerDAO.findById(customerDto.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("Customer not found");
        }

        Customer customer = EntityDTOConverter.convert(customerDto, Customer.class);

        customerDAO.update(customer);
    }
}
