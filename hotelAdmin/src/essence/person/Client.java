package essence.person;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
public class Client implements AbstractClient, Comparable<AbstractClient> {
    private final int id;
    @Setter
    private String fio;
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
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        return id == ((Client) obj).id;
    }

    @Override
    public int compareTo(AbstractClient o) {
        return fio.compareTo(o.getFio());
    }
}
