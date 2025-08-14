package lk.ijse.javafx.drivemax.dao;

import lk.ijse.javafx.drivemax.bo.custom.impl.AttendanceBOImpl;
import lk.ijse.javafx.drivemax.dao.custom.impl.*;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return daoFactory == null ? (daoFactory = new DAOFactory()) : daoFactory;
    }

    public <T extends SuperDAO> T getDAO(DAOTypes daoType) {
        return switch (daoType) {
            case ATTENDANCE -> (T) new AttendanceDAOImpl();
            case CUSTOMER -> (T) new CustomerDAOImpl();
            case EMPLOYEE -> (T) new EmployeeDAOImpl();
            case INVENTORY -> (T) new InventoryDAOImpl();
            case INVOICE -> (T) new InvoiceDAOImpl();
            case OT -> (T) new OTDAOImpl();
            case PAYMENT -> (T) new PaymentDAOImpl();
            case RECORD -> (T) new RecordDAOImpl();
            case REPAIR -> (T) new RepairDAOImpl();
            case SALARY -> (T) new SalaryDAOImpl();
            case SPAREPART -> (T) new SparepartDAOImpl();
            case SUPPLIER -> (T) new SupplierDAOImpl();
            case VEHICLE -> (T) new VehicleDAOImpl();
        };

        }
    }
