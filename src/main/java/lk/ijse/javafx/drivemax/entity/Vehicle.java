package lk.ijse.javafx.drivemax.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Vehicle {
    private String vehId;
    private String cusId;
    private String type;
    private String brand;
    private String regNo;
}
