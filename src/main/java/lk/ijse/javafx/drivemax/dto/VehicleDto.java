package lk.ijse.javafx.drivemax.dto;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VehicleDto {
    private String vehicleId;
    private String customerId;
    private String type;
    private String brand;
    private String regNo;
}