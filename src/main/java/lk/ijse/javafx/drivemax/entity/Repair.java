package lk.ijse.javafx.drivemax.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Repair {
    private String repId;
    private String vehicleId;
    private String employeeId;
    private String work;
    private String cost;
    private String date;
}
