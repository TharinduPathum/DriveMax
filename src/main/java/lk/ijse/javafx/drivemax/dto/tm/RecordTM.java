package lk.ijse.javafx.drivemax.dto.tm;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RecordTM {
    private String recId;
    private String vehicleId;
    private String description;
    private String date;
}
