package lk.ijse.javafx.drivemax.dto.tm;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentTM {
    private String payId;
    private String customerId;
    private String amount;
    private String date;
}
