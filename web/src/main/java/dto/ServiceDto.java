package dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ServiceDto {
    private int id;
    private int price;
    private String name;
    private String status;
    private LocalDateTime serviceTime;
}
