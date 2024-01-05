package room;

import lombok.Getter;
import lombok.Setter;
import person.AbstractClient;
import service.AbstractFavor;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Room extends AbstractFavor implements AbstractRoom, Comparable<AbstractRoom> {
    private final int number;
    private final int capacity;
    private final List<AbstractClient> visitingClients = new ArrayList<>();
    @Setter
    private RoomStatusTypes status = RoomStatusTypes.AVAILABLE;
    private final List<AbstractClient> clientsNowInRoom = new ArrayList<>();
    @Setter
    private int stars;

    public Room(int id, int number, int capacity, int price) {
        super(id, price);
        this.number = number;
        this.capacity = capacity;
    }

    @Override
    public int compareTo(AbstractRoom o) {
        return o.getStars() - stars;
    }

    public String allInfo() {
        return "Room{" +
                "id=" + getId() +
                "stars=" + stars +
                "number=" + number + "; " +
                "capacity=" + capacity + "; " +
                "price=" + getPrice() + "; " +
                "status=" + status + "; " +
                "clientsNowInRoom=" + clientsNowInRoom + "; " +
                "visitingClients=" + visitingClients +
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
