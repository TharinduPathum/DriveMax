package lk.ijse.javafx.drivemax.dto;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VehicleDto {
    private String vehId;
    private String cusId;
    private String type;
    private String brand;
    private String regNo;
}