package lk.ijse.javafx.drivemax.dto;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentDto {
    private String paymentId;
    private String customerId;
    private String amount;
    private String date;
}
