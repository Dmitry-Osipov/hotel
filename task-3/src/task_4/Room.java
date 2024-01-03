package task_4;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Room extends AbstractService {
    private final int number;
    @Setter
    private RoomStatusTypes status = RoomStatusTypes.AVAILABLE;

    public Room(int id, int number, int price) {
        super(id, price);
        this.number = number;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + getId() + "; " +
                "number=" + number + "; " +
                "price=" + getPrice() + "; " +
                "status=" + status +
                '}';
    }
}
