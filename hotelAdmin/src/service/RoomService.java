package service;

import comparators.RoomCapacityComparator;
import comparators.RoomCheckOutTimeComparator;
import comparators.RoomNumberComparator;
import comparators.RoomPriceComparator;
import essence.person.AbstractClient;
import essence.room.AbstractRoom;
import essence.room.Room;
import essence.room.RoomStatusTypes;
import repository.RoomRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Класс отвечает за обработку данных по комнатам.
 */
public class RoomService extends AbstractFavorService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Метод добавляет новую комнату в список всех комнат отеля.
     * @param room Новая комната.
     * @return true, если комната была добавлена, иначе false.
     */
    public boolean addRoom(AbstractRoom room) {
        return roomRepository.getRooms().add(room);
    }

    /**
     * Метод заселяет клиентов в определённую комнату.
     * @param room Комната, в которую требуется заселить клиентов.
     * @param clients Клиенты, которым потребовалось забронировать комнату.
     * @return true, если заселение прошло успешно, иначе false.
     */
    public boolean checkIn(AbstractRoom room, AbstractClient...clients) {
        if (!isValidRoomAndClientsData(room, clients) || !room.getStatus().equals(RoomStatusTypes.AVAILABLE)) {
            return false;
        }

        List<AbstractClient> guests = List.of(clients);
        LocalDateTime now = LocalDateTime.now();
        room.getVisitingClients().addAll(guests);
        room.getClientsNowInRoom().addAll(guests);
        room.setStatus(RoomStatusTypes.OCCUPIED);
        room.setCheckInTime(now);
        guests.forEach(client -> client.setCheckInTime(now));
        return true;
    }

    /**
     * Метод позволяет оценить комнату.
     * @param room Комната, которую требуется оценить.
     * @param stars Оценка (должна быть в пределах от 1 до 5 включительно).
     * @return true, если оценка была добавлена, иначе false.
     */
    public boolean addStarsToRoom(AbstractRoom room, int stars) {
        if (stars < 1 || 5 < stars) {
            return false;
        }
        room.setStars(room.getStars() + stars);
        return true;
    }

    /**
     * * Метод выселения из комнаты.
     * @param room Комната, из которой требуется выселить клиентов.
     * @param clients Клиенты, которых требуется выселить.
     * @return true, если выселение прошло успешно, иначе false.
     */
    public boolean evict(AbstractRoom room, AbstractClient ...clients) {
        if (!isValidRoomAndClientsData(room, clients) || !room.getStatus().equals(RoomStatusTypes.OCCUPIED)) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        for (AbstractClient client : clients) {
            room.getClientsNowInRoom().remove(client);
            client.setCheckOutTime(now);
        }

        if (room.getClientsNowInRoom().isEmpty()) {
            room.setStatus(RoomStatusTypes.AVAILABLE);
            room.setCheckOutTime(now);
        }
        return true;
    }

    /**
     * Метод формирует список всех комнат, отсортированных по убыванию звёзд.
     * @return Отфильтрованный список всех комнат.
     */
    public List<AbstractRoom> roomsByStars() {
        return roomRepository.getRooms()
                .stream()
                .sorted()
                .toList();
    }

    /**
     * Метод формирует список всех комнат, отсортированный по возрастанию цены.
     * @return Отфильтрованный список всех комнат.
     */
    public List<AbstractRoom> roomsByPrice() {
        return roomRepository.getRooms()
                .stream()
                .sorted(new RoomPriceComparator())
                .toList();
    }

    /**
     * Метод формирует список всех комнат, отсортировнный по возрастанию вместимости.
     * @return Отфильтрованный список всех комнат.
     */
    public List<AbstractRoom> roomsByCapacity() {
        return roomRepository.getRooms()
                .stream()
                .sorted(new RoomCapacityComparator())
                .toList();
    }

    /**
     * Метод формирует список свободных комнат, отсортированный по убыванию звёзд.
     * @return Отфильтрованный список свободных комнат.
     */
    public List<AbstractRoom> availableRoomsByStars() {
        return filteredStreamAvailableRooms()
                .sorted()
                .toList();
    }

    /**
     * Метод формирует список свободных комнат, отсортированный по возрастанию цены.
     * @return Отфильтрованный список свободных комнат.
     */
    public List<AbstractRoom> availableRoomsByPrice() {
        return filteredStreamAvailableRooms()
                .sorted(new RoomPriceComparator())
                .toList();
    }

    /**
     * Метод формирует список свободных комнат, отсортированный по возрастанию вместимости.
     * @return Отфильтрованный список свободных комнат.
     */
    public List<AbstractRoom> availableRoomsByCapacity() {
        return filteredStreamAvailableRooms()
                .sorted(new RoomCapacityComparator())
                .toList();
    }

    /**
     * Метод подсчитывает общее число свободных комнат.
     * @return Количество свободных комнат.
     */
    public int countAvailableRooms() {
        return (int) filteredStreamAvailableRooms().count();
    }

    /**
     * Метод формирует список из 3-х последних клиентов комнаты.
     * @param room Комната, которую требуется проверить.
     * @return Список из 3-х последних клиентов.
     */
    public List<AbstractClient> getRoomClients(AbstractRoom room) {
        streamVisitingClientsLimit(room, 3)
                .forEachOrdered(client -> System.out.println("Клиент: " + client.getFio() +
                        " был в номере с: " + client.getCheckInTime() + " по: " + client.getCheckOutTime()));

        return streamVisitingClientsLimit(room, 3).toList();
    }

    /**
     * Метод формирует полную информацию о конкретной комнате.
     * @param room Комната.
     * @return Полная информация про комнату.
     */
    public String getRoomInfo(Room room) {
        return room.allInfo();
    }

    /**
     * Метод формирует список всех комнат конкретного клиента отсортированных по увеличению номера комнаты.
     * @param client Клиент.
     * @return Список комнат.
     */
    public List<AbstractRoom> getClientRoomsByNumbers(AbstractClient client) {
        return getFilteredClientRooms(new RoomNumberComparator(), client);
    }

    /**
     * Метод формирует список всех комнат конкретного клиента с сортировкой по убыванию времени выезда.
     * @param client Клиент.
     * @return Список комнат.
     */
    public List<AbstractRoom> getClientRoomsByCheckOutTime(AbstractClient client) {
        return getFilteredClientRooms(new RoomCheckOutTimeComparator(), client).reversed();
    }

    /**
     * Метод формирует список свободных комнат с конкртеного времени.
     * @param dateTime Время.
     * @return Список свободных комнат.
     */
    public List<AbstractRoom> getAvailableRoomsByTime(LocalDateTime dateTime) {
        return roomRepository.getRooms()
                .stream()
                .filter(room -> room.getCheckOutTime() != null
                        && room.getStatus() == RoomStatusTypes.AVAILABLE
                        && room.getCheckOutTime().isAfter(dateTime))
                .toList();
    }

    /**
     * Служебный метод предназначен для устранения дублирования кода. Метод формирует список последних комнат, которые
     * посетил клиент.
     * @param comparator Компаратор, по которому сортируется список.
     * @param client Клиент.
     * @return Список комнат.
     */
    private List<AbstractRoom> getFilteredClientRooms(Comparator<AbstractRoom> comparator, AbstractClient client) {
        return roomRepository.getRooms()
                .stream()
                .filter(room -> room.getVisitingClients().contains(client))
                .sorted(comparator)
                .toList();
    }

    /**
     * Служебный метод предназначен для устарнения дублирования кода фильтрованного стрима по свободным комнатам.
     * @return Стрим свободных комнат.
     */
    private Stream<AbstractRoom> filteredStreamAvailableRooms() {
        return roomRepository.getRooms()
                .stream()
                .filter(o -> o.getStatus() == RoomStatusTypes.AVAILABLE);
    }

    /**
     * Служебный метод предназначен для устранения дублирования кода лимитированного стрима.
     * @param room Комната.
     * @param limit Лимит количества клиентов, которых требуется получить из истории.
     * @return Стрим списка клиентов, которые последними снимали комнату.
     */
    private Stream<AbstractClient> streamVisitingClientsLimit(AbstractRoom room, int limit) {
        return room.getVisitingClients().reversed()
                .stream()
                .limit(limit);
    }

    /**
     * Служебный метод проверяет валидность поступивших данных о комнате и количестве клиентов.
     * @param room Комната.
     * @param clients Список клиентов.
     * @return true, если проверка пройдена успешно, иначе false.
     */
    private boolean isValidRoomAndClientsData(AbstractRoom room, AbstractClient[] clients) {
        int clientsLength = clients.length;
        return 1 <= clientsLength && clientsLength <= room.getCapacity() && roomRepository.getRooms().contains(room);
    }
}
