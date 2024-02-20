package essence.person;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public class Client implements AbstractClient, Comparable<AbstractClient> {
    private int id;
    private String fio;
    private String phoneNumber;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    public Client() {
    }

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

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id + "; " +
                "fio=" + fio + "; " +
                "phoneNumber=" + phoneNumber + "; " +
                "checkInTime=" + checkInTime + "; " +
                "checkOutTime=" + checkOutTime +
                '}';
    }
}
