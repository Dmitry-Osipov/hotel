package task_4;

import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
public class Hotel {
    private final Set<Room> rooms = new HashSet<>();
    private final Set<Service> services = new HashSet<>();
    private final Set<Client> clients = new HashSet<>();

    public boolean addRoom(Room room) { return rooms.add(room); }

    public boolean addService(Service service) { return services.add(service); }

    public boolean checkIn(Client client, Room room) {
        clients.add(client);
        if (!rooms.contains(room) || !room.getStatus().equals(RoomStatusTypes.AVAILABLE)) return false;
        room.setStatus(RoomStatusTypes.OCCUPIED);
        client.addRoom(room);
        return true;
    }

    public boolean evict(Room room) {
        if (!rooms.contains(room) || !room.getStatus().equals(RoomStatusTypes.OCCUPIED)) return false;
        room.setStatus(RoomStatusTypes.AVAILABLE);
        return true;
    }
}
