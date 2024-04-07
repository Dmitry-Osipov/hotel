package dto;

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
public class ProvidedServiceDto {
    private int id;
    private int clientId;
    private int serviceId;
    private LocalDateTime serviceTime;
}
