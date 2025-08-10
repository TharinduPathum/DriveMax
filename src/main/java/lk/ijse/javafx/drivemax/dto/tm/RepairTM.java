package lk.ijse.javafx.drivemax.dto.tm;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RepairTM {
    private String repId;
    private String vehicleId;
    private String employeeId;
    private String work;
    private String cost;
    private String date;
}
