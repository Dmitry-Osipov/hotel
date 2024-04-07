package dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ProvidedServiceDto {
    private int id;
    private int clientId;
    private int serviceId;
    private LocalDateTime serviceTime;
}
