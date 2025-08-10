package lk.ijse.javafx.drivemax.dto.tm;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InvoiceTM {
    private String invId;
    private String paymentId;
    private String customerId;
    private String description;
    private String date;


}
