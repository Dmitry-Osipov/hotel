package essence.room;

import lombok.Getter;
import lombok.Setter;
import essence.person.AbstractClient;
import essence.service.AbstractFavor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Room extends AbstractFavor implements AbstractRoom, Comparable<AbstractRoom> {
    private final int number;
    private final int capacity;
    private static int count = 1;
    @Setter
    private RoomStatusTypes status = RoomStatusTypes.AVAILABLE;
    @Setter
    private int stars;
    @Setter
    private LocalDateTime checkInTime;
    @Setter
    private LocalDateTime checkOutTime;

    public Room(int number, int capacity, int price) {
        super(count++, price);
        this.number = number;
        this.capacity = capacity;
    }

    @Override
    public int compareTo(AbstractRoom o) {
        return o.getStars() - stars;
    }

    public String allInfo() {
        return "Room{" +
                "id=" + getId() + "; " +
                "stars=" + stars + "; " +
                "number=" + number + "; " +
                "capacity=" + capacity + "; " +
                "price=" + getPrice() + "; " +
                "status=" + status + "; " +
                "check-in time=" + checkInTime + "; " +
                "check-out time=" + checkOutTime + "; " +
                '}';
    }

    @Override
    public String toString() {
        return "Room{" +
                "stars=" + stars + "; " +
                "number=" + number + "; " +
                "capacity=" + capacity + "; " +
                "price=" + getPrice() + "; " +
                "status=" + status +
                '}';
    }
}