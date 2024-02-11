package service;

import essence.Identifiable;
import essence.person.AbstractClient;
import essence.reservation.RoomReservation;
import essence.room.AbstractRoom;
import essence.room.Room;
import essence.room.RoomStatusTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import utils.comparators.RoomCapacityComparator;
import utils.comparators.RoomCheckOutTimeComparator;
import utils.comparators.RoomNumberComparator;
import utils.comparators.RoomPriceComparator;
import utils.csv.FileAdditionResult;
import utils.exceptions.EntityContainedException;
import utils.exceptions.ErrorMessages;
import utils.exceptions.InvalidDataException;
import utils.id.IdFileManager;
import utils.validators.UniqueIdValidator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Класс отвечает за обработку данных по комнатам.
 */
public class RoomService extends AbstractFavorService {
    private static final Logger roomLogger = LoggerFactory.getLogger(RoomService.class);
    private static final Logger reservationLogger = LoggerFactory.getLogger("service.ReservationService");
    private final RoomRepository roomRepository;
    private final RoomReservationRepository reservationRepository;

    public RoomService(RoomRepository roomRepository, RoomReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    /**
     * Метод добавляет новую комнату в список всех комнат отеля.
     * @param room Новая комната.
     * @throws EntityContainedException Ошибка вылетает, когда комната уже содержится в отеле (невозможно повторно
     * добавить).
     */
    public void addRoom(AbstractRoom room) throws EntityContainedException {
        int roomId = room.getId();
        roomLogger.info("Вызван метод добавления комнаты с ID {}", roomId);
        boolean added = roomRepository.getRooms().add(room);

        if (!added) {
            roomLogger.error("Не удалось добавить комнату с ID {}", roomId);
            throw new EntityContainedException(ErrorMessages.ROOM_CONTAINED.getMessage());
        }

        roomLogger.info("Добавлена новая комната с ID {}", roomId);
    }

    /**
     * Метод обновляет данные по комнате.
     * @param room Новые данные комнаты, собранные в классе комнаты.
     * @return {@code true}, если обновить удалось, иначе {@code false}.
     */
    public boolean updateRoom(AbstractRoom room) {
        int roomId = room.getId();
        roomLogger.info("Вызван метод обновления комнаты с ID {}", roomId);
        for (AbstractRoom currentRoom : roomRepository.getRooms()) {
            if (currentRoom.getId() == roomId) {
                currentRoom.setNumber(room.getNumber());
                currentRoom.setCapacity(room.getCapacity());
                currentRoom.setStatus(room.getStatus());
                currentRoom.setPrice(room.getPrice());
                currentRoom.setStars(room.getStars());
                currentRoom.setCheckInTime(room.getCheckInTime());
                currentRoom.setCheckOutTime(room.getCheckOutTime());
                roomLogger.info("Обновлена комната с ID {}", roomId);

                return true;
            }
        }

        roomLogger.error("Не удалось обновить комнату с ID {}", roomId);
        return false;
    }

    /**
     * Метод добавляет резервацию в репозиторий резерваций.
     * @param reservation Резервация для добавления.
     */
    public void addReservation(RoomReservation reservation) {
        int reservationId = reservation.getId();
        reservationLogger.info("Вызван метод добавления новой резервации с ID {}", reservationId);
        reservationRepository.getReservations().add(reservation);
        reservationLogger.info("Добавлена новая резервация с ID {}", reservationId);
    }

    /**
     * Метод обновляет информацию о резервации в репозитории резерваций.
     * @param reservation Обновленная информация о резервации.
     * @return {@code true}, если информация успешно обновлена, иначе {@code false}.
     */
    public boolean updateReservation(RoomReservation reservation) {
        int reservationId = reservation.getId();
        reservationLogger.info("Вызван метод обновления резервации с ID {}", reservationId);
        for (RoomReservation currentReservation : reservationRepository.getReservations()) {
            if (currentReservation.getId() == reservationId) {
                currentReservation.setRoom(reservation.getRoom());
                currentReservation.setCheckInTime(reservation.getCheckInTime());
                currentReservation.setCheckOutTime(reservation.getCheckOutTime());
                currentReservation.setClients(reservation.getClients());
                reservationLogger.info("Обновлена резервация с ID {}", reservationId);

                return true;
            }
        }

        reservationLogger.error("Не удалось обновить резервацию с ID {}", reservationId);
        return false;
    }

    /**
     * Метод заселяет клиентов в определённую комнату.
     * @param room Комната, в которую требуется заселить клиентов.
     * @param clients Клиенты, которым потребовалось забронировать комнату.
     * @throws InvalidDataException Ошибка вылетает, если данные по комнате и клиентам не прошли проверку (клиентов
     * больше вместимости комнаты или меньше 1/комнаты нет в отеле/статус комнаты не "свободен").
     */
    public void checkIn(AbstractRoom room, AbstractClient...clients) throws InvalidDataException {
        String startMessage = "Вызван метод заселения в комнату с ID {} следующих клиентов: {}";
        int roomId = room.getId();
        String clientsString = Arrays.toString(clients);
        roomLogger.info(startMessage, roomId, clientsString);
        reservationLogger.info(startMessage, roomId, clientsString);
        if (!isValidRoomAndClientsData(room, clients) || !room.getStatus().equals(RoomStatusTypes.AVAILABLE)) {
            String message = "Провалена попытка заселения в комнату с ID {} следующих клиентов: {}";
            roomLogger.error(message, roomId, clientsString);
            reservationLogger.error(message, roomId, clientsString);
            throw new InvalidDataException(ErrorMessages.INVALID_DATA.getMessage());
        }

        List<AbstractClient> guests = List.of(clients);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkOutPlan = now.plusHours(22);

        String path = FileAdditionResult.getIdDirectory() + "reservation_id.text";
        int id = IdFileManager.readMaxId(path);
        if (!UniqueIdValidator.validateUniqueId(getReservations(), id)) {
            id = getReservations().stream().mapToInt(Identifiable::getId).max().orElse(0) + 1;
        }
        try {
            IdFileManager.writeMaxId(path, id + 1);
        } catch (IOException e) {
            System.out.println("\n" + FileAdditionResult.FAILURE.getMessage());
        }

        addReservation(new RoomReservation(id, room, now, checkOutPlan, guests));
        room.setStatus(RoomStatusTypes.OCCUPIED);
        room.setCheckInTime(now);
        room.setCheckOutTime(checkOutPlan);
        guests.forEach(client -> client.setCheckInTime(now));
        roomLogger.info("В комнату с ID {} заселены следующие клиенты: {}", roomId, clientsString);
    }

    /**
     * Метод позволяет оценить комнату.
     * @param room Комната, которую требуется оценить.
     * @param stars Оценка (должна быть в пределах от 1 до 5 включительно).
     * @throws InvalidDataException Ошибка вылетает, если было введено количество звёзд меньше 1 или больше 5.
     */
    public void addStarsToRoom(AbstractRoom room, int stars) throws InvalidDataException {
        int roomId = room.getId();
        roomLogger.info("Вызван метод добавления звёзд комнате с ID {}", roomId);
        if (stars < 1 || 5 < stars) {
            roomLogger.error("Звёзды в количестве {} не были добавлены комнате с ID {}", stars, roomId);
            throw new InvalidDataException(ErrorMessages.INVALID_DATA.getMessage());
        }

        room.setStars(room.getStars() + stars);
        roomLogger.info("Звёзды в количестве {} были добавлены комнате с ID {}", stars, roomId);
    }

    /**
     * Метод выселения из комнаты.
     * @param room Комната, из которой требуется выселить клиентов.
     * @param clients Клиенты, которых требуется выселить.
     * @throws InvalidDataException Ошибка вылетает, если данные по комнате и клиентам не прошли проверку (клиентов
     * больше вместимости комнаты или меньше 1/комнаты нет в отеле/статус комнаты не "занят").
     */
    public void evict(AbstractRoom room, AbstractClient ...clients) throws InvalidDataException {
        String startMessage = "Вызван метод выселения из комнаты с ID {} следующих клиентов: {}";
        int roomId = room.getId();
        String clientsString = Arrays.toString(clients);
        roomLogger.info(startMessage, roomId, clientsString);
        reservationLogger.info(startMessage, roomId, clientsString);
        if (!isValidRoomAndClientsData(room, clients) || !room.getStatus().equals(RoomStatusTypes.OCCUPIED)) {
            String message = "Провалена попытка выселения из комнаты с ID {} следующих клиентов: {}";
            roomLogger.error(message, roomId, clientsString);
            reservationLogger.error(message, roomId, clientsString);
            throw new InvalidDataException(ErrorMessages.INVALID_DATA.getMessage());
        }

        LocalDateTime now = LocalDateTime.now();
        List.of(clients).forEach(client -> getReservationByRoom(room).forEach(reservation ->
            client.setCheckOutTime(now)
        ));

        room.setStatus(RoomStatusTypes.AVAILABLE);
        room.setCheckOutTime(now);
        roomLogger.info("Произошло выселение из комнаты с ID {} следующих клиентов: {}",
                roomId, clientsString);
        for (RoomReservation reservation : getReservationByRoom(room).toList()) {
            reservation.setCheckOutTime(now);
            reservationLogger.info("Произошло выселение по резервации с ID {}", reservation.getId());
        }
    }

    /**
     * Метод формирует список всех комнат, отсортированных по убыванию звёзд.
     * @return Отфильтрованный список всех комнат.
     */
    public List<AbstractRoom> roomsByStars() {
        roomLogger.info("Вызван метод получения всех комнат, отсортированных в порядке убывания звёзд");
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
        roomLogger.info("Вызван метод получения всех комнат, отсортированных в порядке возрастания цены");
        return roomRepository.getRooms()
                .stream()
                .sorted(new RoomPriceComparator())
                .toList();
    }

    /**
     * Метод формирует список всех комнат, отсортированный по возрастанию вместимости.
     * @return Отфильтрованный список всех комнат.
     */
    public List<AbstractRoom> roomsByCapacity() {
        roomLogger.info("Вызван метод получения всех комнат, отсортированных в порядке убывания вместимости");
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
        roomLogger.info("Вызван метод получения свободных комнат, отсортированных в порядке убывания звёзд");
        return filteredStreamAvailableRooms()
                .sorted()
                .toList();
    }

    /**
     * Метод формирует список свободных комнат, отсортированный по возрастанию цены.
     * @return Отфильтрованный список свободных комнат.
     */
    public List<AbstractRoom> availableRoomsByPrice() {
        roomLogger.info("Вызван метод получения свободных комнат, отсортированных в порядке возрастания цены");
        return filteredStreamAvailableRooms()
                .sorted(new RoomPriceComparator())
                .toList();
    }

    /**
     * Метод формирует список свободных комнат, отсортированный по возрастанию вместимости.
     * @return Отфильтрованный список свободных комнат.
     */
    public List<AbstractRoom> availableRoomsByCapacity() {
        roomLogger.info("Вызван метод получения свободных комнат, отсортированных в порядке убывания вместимости");
        return filteredStreamAvailableRooms()
                .sorted(new RoomCapacityComparator())
                .toList();
    }

    /**
     * Метод подсчитывает общее число свободных комнат.
     * @return Количество свободных комнат.
     */
    public int countAvailableRooms() {
        roomLogger.info("Вызван метод подсчёта количества свободных комнат");
        return (int) filteredStreamAvailableRooms().count();
    }

    /**
     * Метод формирует список из нескольких последних клиентов комнаты.
     * @param room Комната, которую требуется проверить.
     * @param count Количество людей, которых нужно вывести.
     * @return Список из нескольких последних клиентов.
     */
    public List<AbstractClient> getRoomLastClients(AbstractRoom room, int count) {
        int roomId = room.getId();
        roomLogger.info("Вызван метод формирования списка последних {} клиентов клиентов комнаты с ID {}",
                count, roomId);
        if (count < 1) {
            System.out.println("Полученное количество менее 1. Выводим последнего клиента комнаты:");
            count = 1;
        }

        if (count > streamVisitingClientsLimit(room, count).count()) {
            System.out.println("Введено количество, которое больше объёма списка. " +
                    "Выводим всех последних клиентов комнаты:");
        }

        List<AbstractClient> list = streamVisitingClientsLimit(room, count).toList();
        roomLogger.info("Для комнаты с ID {} получен следующий список последних {} клиентов: {}", roomId, count, list);
        return list;
    }

    /**
     * Метод формирует полную информацию о конкретной комнате.
     * @param room Комната.
     * @return Полная информация про комнату.
     */
    public String getRoomInfo(Room room) {
        roomLogger.info("Вызван метод получения информации по комнате с ID {}", room.getId());
        return room.toString();
    }

    /**
     * Метод формирует список всех комнат конкретного клиента отсортированных по увеличению номера комнаты.
     * @param client Клиент.
     * @return Список комнат.
     */
    public List<AbstractRoom> getClientRoomsByNumbers(AbstractClient client) {
        int clientId = client.getId();
        roomLogger.info("Вызван метод получения всех комнат, отсортированных в порядке возрастания номера комнаты, " +
                "для клиента с ID {}", clientId);
        List<AbstractRoom> rooms = getFilteredClientRooms(new RoomNumberComparator(), client);
        roomLogger.info("Для клиента с ID {} получен следующий список последних комнат, отсортированный в порядке " +
                "возрастания номера комнаты: {}", clientId, rooms);
        return rooms;
    }

    /**
     * Метод формирует список всех комнат конкретного клиента с сортировкой по убыванию времени выезда.
     * @param client Клиент.
     * @return Список комнат.
     */
    public List<AbstractRoom> getClientRoomsByCheckOutTime(AbstractClient client) {
        int clientId = client.getId();
        roomLogger.info("Вызван метод получения всех комнат, отсортированных в порядке убывания времени выезда, " +
                "для клиента с ID {}", clientId);
        List<AbstractRoom> rooms = getFilteredClientRooms(new RoomCheckOutTimeComparator(), client).reversed();
        roomLogger.info("Для клиента с ID {} получен следующий список последних комнат, отсортированный в порядке " +
                "убывания времени выезда: {}", clientId, rooms);
        return rooms;
    }

    /**
     * Метод формирует список свободных комнат с конкретного времени.
     * @param dateTime Время.
     * @return Список свободных комнат.
     */
    public List<AbstractRoom> getAvailableRoomsByTime(LocalDateTime dateTime) {
        roomLogger.info("Вызван метод получения свободных комнат, начиная с: {}", dateTime);
        List<AbstractRoom> rooms = roomRepository.getRooms()
                .stream()
                .filter(room -> room.getCheckOutTime() != null
                        && room.getStatus() == RoomStatusTypes.AVAILABLE
                        && room.getCheckOutTime().isAfter(dateTime))
                .toList();
        roomLogger.info("По времени {} получен следующий список свободных комнат: {}", dateTime, rooms);
        return rooms;
    }

    /**
     * Метод получения списка всех резерваций.
     * @return Список резерваций.
     */
    public List<RoomReservation> getReservations() {
        reservationLogger.info("Вызов метода получения списка резерваций");
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
     * Служебный метод предназначен для устранения дублирования кода фильтрованного стрима по свободным комнатам.
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
