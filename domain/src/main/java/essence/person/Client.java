package essence.person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс клиента.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "hotel", name = "client")
public class Client implements AbstractClient, Comparable<AbstractClient> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fio")
    private String fio;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
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
