package lk.ijse.javafx.drivemax.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Employee {
    private String id;
    private String name;
    private String speciality;
    private String address;
    private String email;
    private String phone;
}
