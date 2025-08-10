package lk.ijse.javafx.drivemax.dto;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RepairDto {
    private String repairId;
    private String vehicleId;
    private String employeeId;
    private String work;
    private String cost;
    private String date;
}
