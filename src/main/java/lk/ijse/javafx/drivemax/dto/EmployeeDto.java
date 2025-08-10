package lk.ijse.javafx.drivemax.dto;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeDto {
    private String employeeId;
    private String name;
    private String speciality;
    private String address;
    private String email;
    private String phone;
}