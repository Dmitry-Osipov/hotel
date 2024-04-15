package dto;

import essence.Identifiable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Класс, представляющий сущность клиента с информацией о клиенте.
 * Используется для передачи данных о клиенте между слоями приложения.
 */
@Getter
@Setter
@ToString
public class ClientDto implements Identifiable {
    private int id;
    @Size(min = 5, message = "FIO must be minimum 5 symbols")
    private String fio;
    @Pattern(regexp = "\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}", message = "Please use pattern +7(XXX)XXX-XX-XX")
    private String phoneNumber;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
}
