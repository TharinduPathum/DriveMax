package lk.ijse.javafx.drivemax.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Invoice {
    private String invId;
    private String paymentId;
    private String customerId;
    private String description;
    private String date;
}
