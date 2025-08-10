package lk.ijse.javafx.drivemax.model;

import lk.ijse.javafx.drivemax.db.DBConnection;
import lk.ijse.javafx.drivemax.dto.SupplierDto;
import lk.ijse.javafx.drivemax.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class SupplierModel {
    public ArrayList<SupplierDto> getAllSupplier() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("select * from supplier");

        ArrayList<SupplierDto> list = new ArrayList<>();
        while (resultSet.next()) {
            SupplierDto supplierDto = new SupplierDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );

            list.add(supplierDto);
        }
        return list;
    }

    public String getNextId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT sup_id FROM supplier ORDER BY sup_id DESC LIMIT 1");
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

    public ArrayList<String> getAllSupplierIds() throws SQLException {
        String sql = "SELECT sup_id FROM supplier";

        ResultSet rs = CrudUtil.execute(sql);
        ArrayList<String> idList = new ArrayList<>();

        while (rs.next()) {
            idList.add(rs.getString("sup_id"));
        }

        return idList;
    }

    public String getSupplierEmailById(String supId) throws SQLException {
        String sql = "SELECT email FROM supplier WHERE sup_id = ?";
        ResultSet rs = CrudUtil.execute(sql, supId);

        if (rs.next()) {
            return rs.getString("email");
        }

        return null;
    }

    public boolean saveSupplier(SupplierDto supplierDto) throws SQLException {

        return CrudUtil.execute(
                "insert into supplier values (?,?,?,?,?)",
                supplierDto.getSupplierId(),
                supplierDto.getName(),
                supplierDto.getAddress(),
                supplierDto.getEmail(),
                supplierDto.getPhone()
        );
    }

    public boolean updateSupplier(SupplierDto supplierDto) throws SQLException {

        return CrudUtil.execute(
                "UPDATE supplier SET name = ?, address = ?, email = ?, phone = ? WHERE sup_id = ?",

                supplierDto.getName(),
                supplierDto.getAddress(),
                supplierDto.getEmail(),
                supplierDto.getPhone(),
                supplierDto.getSupplierId()



        );
    }

    public boolean deleteSupplier(String supplierId) throws SQLException {
        String sql = "DELETE FROM supplier WHERE sup_id = ?";
        return CrudUtil.execute(sql, supplierId);
    }


}