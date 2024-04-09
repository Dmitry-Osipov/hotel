package dto;

import essence.Identifiable;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Класс, представляющий информацию об услуге, предоставленной клиенту.
 * Используется для передачи данных о предоставленной услуге между слоями приложения.
 */
@Getter
@Setter
@ToString
public class ProvidedServiceDto implements Identifiable {
    private int id;
    @Positive(message = "Client ID must be a positive number")
    private int clientId;
    @Positive(message = "Service ID must be a positive number")
    private int serviceId;
    @Null(message = "The date must be a null")
    private LocalDateTime serviceTime;
}
