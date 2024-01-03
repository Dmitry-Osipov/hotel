package task_4;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Client {
    private final int id;
    private final String fio;
    @Setter
    private String phoneNumber;
    private final List<Room> occupiedRooms = new ArrayList<>();

    public Client(int id, String fio, String phoneNumber) {
        this.id = id;
        this.fio = fio;
        this.phoneNumber = phoneNumber;
    }

    public boolean addRoom(Room room) { return occupiedRooms.add(room); }
}
