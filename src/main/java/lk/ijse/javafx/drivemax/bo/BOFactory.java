package lk.ijse.javafx.drivemax.bo;

import lk.ijse.javafx.drivemax.bo.custom.impl.*;


public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getInstance() {
        return boFactory == null ? (boFactory = new BOFactory()) : boFactory;
    }

    @SuppressWarnings("unchecked")
    public <Hello extends SuperBO> Hello getBO(BOTypes boType) {
        return switch (boType) {
            case ATTENDANCE -> (Hello) new AttendanceBOImpl();
            case CUSTOMER -> (Hello)new CustomerBOImpl();
            case EMPLOYEE -> (Hello) new EmployeeBOImpl();
            case INVENTORY -> (Hello) new InventoryBOImpl();
            case INVOICE -> (Hello) new InvoiceBOImpl();
            case OT -> (Hello) new OTBOImpl();
            case PAYMENT -> (Hello) new PaymentBOImpl();
            case RECORD -> (Hello) new RecordBOImpl();
            case REPAIR -> (Hello) new RepairBOImpl();
            case SALARY -> (Hello) new SalaryBOImpl();
            case SPAREPART -> (Hello) new SparepartBOImpl();
            case SUPPLIER -> (Hello) new SupplierBOImpl();
            case VEHICLE -> (Hello) new VehicleBOImpl();
            default -> throw new IllegalArgumentException("Invalid BO Type: " + boType);
        };
    }
}
