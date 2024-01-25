package essence.room;

import essence.service.AbstractFavor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
public class Room extends AbstractFavor implements AbstractRoom, Comparable<AbstractRoom> {
    private int number;
    private int capacity;
    private RoomStatusTypes status = RoomStatusTypes.AVAILABLE;
    private int stars;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    public Room(int id, int number, int capacity, int price) {
        super(id, price);
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
