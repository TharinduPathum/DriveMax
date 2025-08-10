package lk.ijse.javafx.drivemax.dto;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RecordDto {
    private String recordId;
    private String vehicleId;
    private String description;
    private String date;
}
