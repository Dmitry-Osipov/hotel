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

    /**
     * Метод добавляет новую комнату в список всех комнат отеля.
     * @param room Новая комната.
     * @return true, если комната была добавлена, иначе false.
     */
    public boolean addRoom(AbstractRoom room) { return rooms.add(room); }

    /**
     * Метод добавляет новую услугу в список всех услуг отеля.
     * @param service Новая услуга.
     * @return true, если услуга была добавлена, иначе false.
     */
    public boolean addService(AbstractService service) { return services.add(service); }

    /**
     * Метод заселяет клиента в определённую комнату.
     * @param client Клиент, которому потребовалось забронировать комнату.
     * @param room Комната для бронирования.
     * @return true, если заселение прошло успешно, иначе false.
     */
    public boolean checkIn(AbstractClient client, AbstractRoom room) {
        clients.add(client);
        if (!rooms.contains(room) || !room.getStatus().equals(RoomStatusTypes.AVAILABLE)) return false;
        room.setStatus(RoomStatusTypes.OCCUPIED);
        client.addRoom(room);
        return true;
    }

    /**
     * Метод выселения из комнаты.
     * @param room Комната, из которой требуется выселить клиента,
     * @return true, если выселить удалось, иначе false.
     */
    public boolean evict(AbstractRoom room) {
        if (!rooms.contains(room) || !room.getStatus().equals(RoomStatusTypes.OCCUPIED)) return false;
        room.setStatus(RoomStatusTypes.AVAILABLE);
        return true;
    }
}
