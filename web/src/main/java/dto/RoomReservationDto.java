package dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class RoomReservationDto {
    private int id;
    private List<Integer> clientIds;
    private int roomId;
    LocalDateTime checkInTime;
    LocalDateTime checkOutTime;
}
