package task_4;

import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
public class Hotel {
    private final Set<AbstractRoom> rooms = new HashSet<>();
    private final Set<AbstractService> services = new HashSet<>();
    private final Set<AbstractClient> clients = new HashSet<>();

    public boolean addRoom(AbstractRoom room) { return rooms.add(room); }

    public boolean addService(AbstractService service) { return services.add(service); }

    public boolean checkIn(AbstractClient client, AbstractRoom room) {
        clients.add(client);
        if (!rooms.contains(room) || !room.getStatus().equals(RoomStatusTypes.AVAILABLE)) return false;
        room.setStatus(RoomStatusTypes.OCCUPIED);
        client.addRoom(room);
        return true;
    }

    public boolean evict(AbstractRoom room) {
        if (!rooms.contains(room) || !room.getStatus().equals(RoomStatusTypes.OCCUPIED)) return false;
        room.setStatus(RoomStatusTypes.AVAILABLE);
        return true;
    }
}
