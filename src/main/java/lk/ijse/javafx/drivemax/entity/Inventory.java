package lk.ijse.javafx.drivemax.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Inventory {
    private String spId;
    private String supplierId;
    private String brand;
    private String name;
    private String amount;
    private String quantity;
}
