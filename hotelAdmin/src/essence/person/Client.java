package essence.person;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class Client implements AbstractClient, Comparable<AbstractClient> {
    private final int id;
    private final String fio;
    @Setter
    private String phoneNumber;
    @Setter
    private LocalDateTime checkInTime;
    @Setter
    private LocalDateTime checkOutTime;

    public Client(int id, String fio, String phoneNumber) {
        this.id = id;
        this.fio = fio;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int compareTo(AbstractClient o) {
        return fio.compareTo(o.getFio());
    }
}
