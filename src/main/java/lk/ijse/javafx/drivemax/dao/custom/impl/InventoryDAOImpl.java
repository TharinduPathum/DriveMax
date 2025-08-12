package lk.ijse.javafx.drivemax.dao.custom.impl;

import lk.ijse.javafx.drivemax.dao.SQLUtil;
import lk.ijse.javafx.drivemax.dao.custom.InventoryDAO;
import lk.ijse.javafx.drivemax.dto.InventoryDto;
import lk.ijse.javafx.drivemax.entity.Inventory;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryDAOImpl implements InventoryDAO {
    @Override
    public List<Inventory> getAll() throws SQLException {

            ResultSet resultSet = SQLUtil.execute("select * from inventory");

            List<Inventory> list = new ArrayList<>();
            while (resultSet.next()) {
                Inventory inventory = new Inventory(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6)
                );

                list.add(inventory);
            }
            return list;
    }

    @Override
    public String getNextId() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT sp_id FROM inventory ORDER BY sp_id DESC LIMIT 1");
        String prefix = "SP";

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
    public boolean save(Inventory inventory) throws SQLException {
        return SQLUtil.execute(
                "insert into inventory values (?,?,?,?,?,?)",
                inventory.getSpId(),
                inventory.getSupplierId(),
                inventory.getBrand(),
                inventory.getName(),
                inventory.getAmount(),
                inventory.getQuantity()

        );
    }

    @Override
    public boolean update(Inventory inventory) throws SQLException {
        return SQLUtil.execute(
                "UPDATE inventory SET sup_id=?, brand=?, name=?, amount=?, quantity=? WHERE sp_id=?",

                inventory.getSupplierId(),
                inventory.getBrand(),
                inventory.getName(),
                inventory.getAmount(),
                inventory.getQuantity(),
                inventory.getSpId()
        );
    }

    @Override
    public boolean delete(String spId) throws SQLException {
        String sql = "DELETE FROM inventory WHERE sp_id=?";
        return SQLUtil.execute(sql,spId);
    }

    @Override
    public List<String> getAllIds() throws SQLException {
        return List.of();
    }

    @Override
    public Optional<Inventory> findById(String id) throws SQLException {
        return Optional.empty();
    }
}
