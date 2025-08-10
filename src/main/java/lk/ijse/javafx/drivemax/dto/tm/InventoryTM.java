package lk.ijse.javafx.drivemax.dto.tm;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InventoryTM {
    private String spId;
    private String supplierId;
    private String brand;
    private String name;
    private String amount;
    private String quantity;
}
