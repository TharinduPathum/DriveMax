package lk.ijse.javafx.drivemax.model;

import lk.ijse.javafx.drivemax.dto.InventoryDto;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class InventoryModel {
    public ArrayList<InventoryDto> getAllSparePart() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("select * from inventory");

        ArrayList<InventoryDto> list = new ArrayList<>();
        while (resultSet.next()) {
            InventoryDto inventoryDto = new InventoryDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            );

            list.add(inventoryDto);
        }
        return list;
    }

    public String getNextId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT sp_id FROM inventory ORDER BY sp_id DESC LIMIT 1");
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

    public boolean saveSparePart(InventoryDto inventoryDto) throws SQLException {

     return CrudUtil.execute(
                "insert into inventory values (?,?,?,?,?,?)",
                inventoryDto.getSparePartId(),
                inventoryDto.getSupplierId(),
                inventoryDto.getBrand(),
                inventoryDto.getName(),
                inventoryDto.getAmount(),
                inventoryDto.getQuantity()

        );
    }

    public boolean updateSparePart(InventoryDto inventoryDto) throws SQLException {
        return CrudUtil.execute(
                "UPDATE inventory SET sup_id=?, brand=?, name=?, amount=?, quantity=? WHERE sp_id=?",

                inventoryDto.getSupplierId(),
                inventoryDto.getBrand(),
                inventoryDto.getName(),
                inventoryDto.getAmount(),
                inventoryDto.getQuantity(),
                inventoryDto.getSparePartId()
        );
    }

    public boolean deleteSparePart(String spId) throws SQLException {
        String sql = "DELETE FROM inventory WHERE sp_id=?";
        return CrudUtil.execute(sql, spId);
    }

}