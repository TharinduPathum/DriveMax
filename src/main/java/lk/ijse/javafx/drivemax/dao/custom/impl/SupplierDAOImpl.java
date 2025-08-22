package lk.ijse.javafx.drivemax.dao.custom.impl;

import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.SupplierDAO;
import lk.ijse.javafx.drivemax.dto.SupplierDto;
import lk.ijse.javafx.drivemax.entity.Employee;
import lk.ijse.javafx.drivemax.entity.Supplier;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplierDAOImpl implements SupplierDAO {

    @Override
    public List<Supplier> getAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select * from supplier");

        List<Supplier> list = new ArrayList<>();
        while (resultSet.next()) {
            Supplier supplier = new Supplier(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );

            list.add(supplier);
        }
        return list;
    }

    @Override
    public String getNextId() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT sup_id FROM supplier ORDER BY sup_id DESC LIMIT 1");
        String prefix = "SUP";

        if (resultSet.next()) {
            String lastId = resultSet.getString(1);

            String lastIdNumberString = lastId.substring(prefix.length());

            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;

            String nextIdString = String.format("%s%03d", prefix, nextIdNumber);
            return nextIdString;
        }

        return prefix + "001";
    }

    @Override
    public boolean save(Supplier supplier) throws SQLException {
        return SQLUtil.execute(
                "insert into supplier values (?,?,?,?,?)",
                supplier.getId(),
                supplier.getName(),
                supplier.getAddress(),
                supplier.getEmail(),
                supplier.getPhone()
        );
    }

    @Override
    public boolean update(Supplier supplier) throws SQLException {
        return SQLUtil.execute(
                "UPDATE supplier SET name = ?, address = ?, email = ?, phone = ? WHERE sup_id = ?",

                supplier.getName(),
                supplier.getAddress(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getId()



        );
    }

    @Override
    public boolean delete(String supplierId) throws SQLException {
        String sql = "DELETE FROM supplier WHERE sup_id = ?";
        return SQLUtil.execute(sql, supplierId);
    }

    @Override
    public List<String> getAllIds() throws SQLException {
        String sql = "SELECT sup_id FROM supplier";

        ResultSet rs = SQLUtil.execute(sql);
        ArrayList<String> idList = new ArrayList<>();

        while (rs.next()) {
            idList.add(rs.getString("sup_id"));
        }

        return idList;
    }

    @Override
    public Optional<Supplier> findById(String id) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM supplier WHERE sup_id = ?", id);
        if (resultSet.next()) {
            return Optional.of(new Supplier(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return Optional.empty();

    }

    @Override
    public String getSupplierEmailById(String supId) throws SQLException {
        String sql = "SELECT email FROM supplier WHERE sup_id = ?";
        ResultSet rs = SQLUtil.execute(sql, supId);

        if (rs.next()) {
            return rs.getString("email");
        }

        return null;
    }

    @Override
    public Optional<String> getLastSupplierId() throws SQLException {
        String sql = "SELECT sup_id FROM supplier ORDER BY sup_id DESC LIMIT 1";
        ResultSet rs = SQLUtil.execute(sql);

        if (rs.next()) {
            return Optional.of(rs.getString("sup_id"));
        }
        return Optional.empty();
    }
}
