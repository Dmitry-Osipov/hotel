package task4;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Client implements AbstractClient {
    private final int id;
    private final String fio;
    @Setter
    private String phoneNumber;
    private final List<AbstractRoom> occupiedRooms = new ArrayList<>();

    public Client(int id, String fio, String phoneNumber) {
        this.id = id;
        this.fio = fio;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean addRoom(AbstractRoom room) { return occupiedRooms.add(room); }
}
