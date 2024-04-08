package dto;

import essence.Identifiable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Класс, представляющий информацию об услуге.
 * Используется для передачи данных об услуге между слоями приложения.
 */
@Getter
@Setter
@ToString
public class ServiceDto implements Identifiable {
    private int id;
    @Min(value = 1500, message = "Must be greater than 1499")
    private int price;
    @NotBlank(message = "Name is required field")
    private String name;
    @NotBlank(message = "Status is required field")
    private String status;
    private LocalDateTime serviceTime;
}
