package lk.ijse.javafx.drivemax.dto;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupplierDto {
    private String supplierId;
    private String name;
    private String address;
    private String email;
    private String phone;
}