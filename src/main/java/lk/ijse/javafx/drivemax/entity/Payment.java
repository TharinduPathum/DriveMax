package lk.ijse.javafx.drivemax.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Payment {
    private String payId;
    private String customerId;
    private String amount;
    private String date;
}
