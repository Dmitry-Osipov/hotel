package dto;

import essence.Identifiable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
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
    @NotEmpty(message = "List of clients must be not empty")
    private List<Integer> clientIds;
    @Positive(message = "Room ID must be a positive number")
    private int roomId;
    LocalDateTime checkInTime;
    LocalDateTime checkOutTime;
}
