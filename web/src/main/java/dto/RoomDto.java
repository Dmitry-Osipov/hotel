package dto;

import essence.Identifiable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Класс, представляющий информацию о номере гостиницы.
 * Используется для передачи данных о номере между слоями приложения.
 */
@Getter
@Setter
@ToString
public class RoomDto implements Identifiable {
    private int id;
    @Min(value = 1500, message = "Must be greater than 1499")
    private int price;
    @Positive(message = "Number must be a positive number")
    private int number;
    @Positive(message = "Capacity must be a positive number")
    @Max(value = 8, message = "Must less then 9")
    private int capacity;
    @NotBlank(message = "Status is required field")
    private String status;
    @Min(value = 0, message = "Must be greater than -1")
    @Max(value = 5, message = "Must less then 6")
    private int stars;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
}
