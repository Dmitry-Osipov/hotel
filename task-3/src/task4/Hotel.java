package task4;

import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ToString
public class Hotel {
    private final Set<AbstractRoom> rooms = new HashSet<>();
    private final Set<AbstractService> services = new HashSet<>();
    private final Set<AbstractClient> clients = new HashSet<>();

    /**
     * Метод добавляет новую комнату в список всех комнат отеля.
     * @param room Новая комната.
     * @return true, если комната была добавлена, иначе false.
     */
    public boolean addRoom(AbstractRoom room) {
        return rooms.add(room);
    }

    /**
     * Метод добавляет новую услугу в список всех услуг отеля.
     * @param service Новая услуга.
     * @return true, если услуга была добавлена, иначе false.
     */
    public boolean addService(AbstractService service) {
        return services.add(service);
    }

    /**
     * Метод заселяет клиента в определённую комнату.
     * @param room Комната, в которую требуется заселить клиентов.
     * @param clients Клиенты, которым потребовалось забронировать комнату.
     * @return true, если заселение прошло успешно, иначе false.
     */
    public boolean checkIn(AbstractRoom room, AbstractClient ...clients) {
        int clientsLength = clients.length;
        if (clientsLength < 1 || room.getCapacity() < clientsLength) {
            return false;
        }

        if (!rooms.contains(room) || !room.getStatus().equals(RoomStatusTypes.AVAILABLE)) {
            return false;
        }

        List<AbstractClient> guests = List.of(clients);
        this.clients.addAll(guests);
        room.getClientsNowInRoom().addAll(guests);
        room.setStatus(RoomStatusTypes.OCCUPIED);
        return true;
    }

    /**
     * * Метод выселения из комнаты.
     * @param room Комната, из которой требуется выселить клиентов.
     * @param clients Клиенты, которых требуется выселить.
     * @return true, если выселение прошло успешно, иначе false.
     */
    public boolean evict(AbstractRoom room, AbstractClient ...clients) {
        int clientsLength = clients.length;
        if (clientsLength < 1 || room.getCapacity() < clientsLength) {
            return false;
        }

        if (!rooms.contains(room) || !room.getStatus().equals(RoomStatusTypes.OCCUPIED)) {
            return false;
        }

        for (AbstractClient client : clients) {
            room.getClientsNowInRoom().remove(client);
        }

        if (room.getClientsNowInRoom().isEmpty()) {
            room.setStatus(RoomStatusTypes.AVAILABLE);
        }
        return true;
    }
}
