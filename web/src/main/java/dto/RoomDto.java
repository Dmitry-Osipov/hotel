package dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class RoomDto {
    private int id;
    private int price;
    private int number;
    private int capacity;
    private String status;
    private int stars;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
}
