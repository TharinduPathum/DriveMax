package lk.ijse.javafx.drivemax.dto;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AttendanceDto {
    private String empId;
    private String date;
    private String status;

}
