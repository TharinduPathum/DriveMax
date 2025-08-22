package lk.ijse.javafx.drivemax.dto;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RecordDto {
    private String recId;
    private String vehicleId;
    private String description;
    private String date;
}
