package lk.ijse.javafx.drivemax.dto;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SalaryDto {
    private String salaryId;
    private String employeeId;
    private String attendance;
    private String otWork;
    private String dsalary;
    private String msalary;
    private String date;

}