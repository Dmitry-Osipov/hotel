package task4;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Room extends AbstractFavor implements AbstractRoom {
    private final int number;
    private final int capacity;
    private final List<AbstractClient> visitingClients = new ArrayList<>();
    @Setter
    private RoomStatusTypes status = RoomStatusTypes.AVAILABLE;
    private final List<AbstractClient> clientsNowInRoom = new ArrayList<>();

    public Room(int id, int number, int capacity, int price) {
        super(id, price);
        this.number = number;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + getId() + "; " +
                "number=" + number + "; " +
                "capacity=" + capacity + "; " +
                "price=" + getPrice() + "; " +
                "status=" + status +
                '}';
    }
}
