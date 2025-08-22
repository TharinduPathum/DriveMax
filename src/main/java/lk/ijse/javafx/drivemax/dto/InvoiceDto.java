package lk.ijse.javafx.drivemax.dto;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InvoiceDto {
    private String invId;
    private String paymentId;
    private String customerId;
    private String description;
    private String date;


}
