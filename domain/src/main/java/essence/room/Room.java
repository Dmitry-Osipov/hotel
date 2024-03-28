package essence.room;

import essence.service.AbstractFavor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * Класс комнаты.
 */
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(schema = "hotel", name = "room")
public class Room extends AbstractFavor implements AbstractRoom, Comparable<AbstractRoom> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "price")
    private int price;

    @Column(name = "number")
    private int number;

    @Column(name = "capacity")
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RoomStatusTypes status = RoomStatusTypes.AVAILABLE;

    @Column(name = "stars")
    private int stars;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    public Room(int id, int number, int capacity, int price) {
        this.id = id;
        this.price = price;
        this.number = number;
        this.capacity = capacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        return getId() == ((Room) obj).getId();
    }

    @Override
    public int compareTo(AbstractRoom o) {
        return o.getStars() - stars;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + getId() + "; " +
                "stars=" + stars + "; " +
                "number=" + number + "; " +
                "capacity=" + capacity + "; " +
                "price=" + getPrice() + "; " +
                "status=" + status + "; " +
                "check-in time=" + checkInTime + "; " +
                "check-out time=" + checkOutTime +
                '}';
    }
}
