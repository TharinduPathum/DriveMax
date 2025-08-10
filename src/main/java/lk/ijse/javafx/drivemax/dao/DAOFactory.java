package lk.ijse.javafx.drivemax.dao;

import lk.ijse.javafx.drivemax.dao.custom.impl.CustomerDAOImpl;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return daoFactory == null ? (daoFactory = new DAOFactory()) : daoFactory;
    }

    public <T extends SuperDAO> T getDAO(DAOTypes daoType) {
        return switch (daoType) {
            case ATTENDANCE -> null;
            case CUSTOMER -> (T) new CustomerDAOImpl();
            case EMAIL -> null;
            case EMPLOYEE -> null;
            case FEEDBACK -> null;
            case INVENTORY -> null;
            case INVOICE -> null;
            case OT -> null;
            case PAYMENT -> null;
            case RECORD -> null;
            case REPAIR -> null;
            case SALARY -> null;
            case SPAREPART -> null;
            case SUPPLIER -> null;
            case VEHICLE -> null;
        };

        }
    }
