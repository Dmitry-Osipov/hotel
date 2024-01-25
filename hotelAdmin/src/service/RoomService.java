package service;

import comparators.RoomCapacityComparator;
import comparators.RoomCheckOutTimeComparator;
import comparators.RoomNumberComparator;
import comparators.RoomPriceComparator;
import essence.person.AbstractClient;
import essence.reservation.RoomReservation;
import essence.room.AbstractRoom;
import essence.room.Room;
import essence.room.RoomStatusTypes;
import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Класс отвечает за обработку данных по комнатам.
 */
public class RoomService extends AbstractFavorService {
    private final RoomRepository roomRepository;
    private final RoomReservationRepository reservationRepository;

    public RoomService(RoomRepository roomRepository, RoomReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
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
     * Метод обновляет данные по комнате.
     * @param room Новые данные комнаты, собранные в классе комнаты.
     * @return true, если обновить удалось, иначе false.
     */
    public boolean updateRoom(AbstractRoom room) {
        for (AbstractRoom currentRoom : roomRepository.getRooms()) {
            if (currentRoom.getId() == room.getId()) {
                currentRoom.setNumber(room.getNumber());
                currentRoom.setCapacity(room.getCapacity());
                currentRoom.setStatus(room.getStatus());
                currentRoom.setPrice(room.getPrice());
                currentRoom.setStars(room.getStars());
                currentRoom.setCheckInTime(room.getCheckInTime());
                currentRoom.setCheckOutTime(room.getCheckOutTime());

                return true;
            }
        }

        return false;
    }

    /**
     * Метод заселяет клиентов в определённую комнату.
     * @param id Уникальный идентификатор резервации.
     * @param room Комната, в которую требуется заселить клиентов.
     * @param clients Клиенты, которым потребовалось забронировать комнату.
     * @return true, если заселение прошло успешно, иначе false.
     */
    public boolean checkIn(int id, AbstractRoom room, AbstractClient...clients) {
        if (!isValidRoomAndClientsData(room, clients) || !room.getStatus().equals(RoomStatusTypes.AVAILABLE)) {
            return false;
        }

        List<AbstractClient> guests = List.of(clients);
        LocalDateTime now = LocalDateTime.now();
        reservationRepository.getReservations().add(new RoomReservation(id, room, now, now.plusHours(22), guests));
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
        List.of(clients).forEach(client -> getReservationByRoom(room).forEach(reservation ->
            client.setCheckOutTime(now)
        ));

        room.setStatus(RoomStatusTypes.AVAILABLE);
        room.setCheckOutTime(now);
        getReservationByRoom(room).forEach(reservation -> reservation.setCheckOutTime(now));

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
     * Метод формирует список из нескольких последних клиентов комнаты.
     * @param room Комната, которую требуется проверить.
     * @param count Количество людей, которых нужно вывести.
     * @return Список из нескольких последних клиентов.
     */
    public List<AbstractClient> getRoomLastClients(AbstractRoom room, int count) {
        if (count < 1) {
            System.out.println("Полученное количество менее 1. Выводим последнего клиента комнаты:");
            count = 1;
        }

        if (count > streamVisitingClientsLimit(room, count).count()) {
            System.out.println("Введено количество, которое больше объёма списка. " +
                    "Выводим всех последних клиентов комнаты:");
        }

        return streamVisitingClientsLimit(room, count).toList();
    }

    /**
     * Метод формирует полную информацию о конкретной комнате.
     * @param room Комната.
     * @return Полная информация про комнату.
     */
    public String getRoomInfo(Room room) {
        return room.toString();
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
     * Метод получения списка всех резерваций.
     * @return Список резерваций.
     */
    public List<RoomReservation> getReservations() {
        return reservationRepository.getReservations();
    }

    /**
     * Служебный метод предназначен для снижения дублирования кода создания стрима, отфильтрованного по сравнению комнат.
     * @param room Комната.
     * @return Стрим, содержащий список резерваций. Содержит только резерв, который содержит эту комнату.
     */
    private Stream<RoomReservation> getReservationByRoom(AbstractRoom room) {
        return reservationRepository.getReservations()
                .stream()
                .filter(reservation -> reservation.getRoom().equals(room));
    }

    /**
     * Служебный метод предназначен для устранения дублирования кода. Метод формирует список последних комнат, которые
     * посетил клиент.
     * @param comparator Компаратор, по которому сортируется список.
     * @param client Клиент.
     * @return Список комнат.
     */
    private List<AbstractRoom> getFilteredClientRooms(Comparator<RoomReservation> comparator, AbstractClient client) {
        return reservationRepository.getReservations()
                .stream()
                .filter(reservation -> reservation.getClients().contains(client))
                .sorted(comparator)
                .map(RoomReservation::getRoom)
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
        return reservationRepository.getReservations()
                .stream()
                .filter(reservation -> reservation.getRoom().equals(room))
                .flatMap(reservation -> reservation.getClients().stream())
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
