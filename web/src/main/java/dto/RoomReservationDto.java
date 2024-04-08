package dto;

import essence.Identifiable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс, представляющий информацию о бронировании номера гостиницы.
 * Используется для передачи данных о бронировании между слоями приложения.
 */
@Getter
@Setter
@ToString
public class RoomReservationDto implements Identifiable {
    private int id;
    private List<Integer> clientIds;
    private int roomId;
    LocalDateTime checkInTime;
    LocalDateTime checkOutTime;
}
