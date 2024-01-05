package view;

import controller.Hotel;
import comparators.RoomCapacityComparator;
import comparators.RoomPriceComparator;
import person.AbstractClient;
import room.AbstractRoom;
import room.Room;
import room.RoomStatusTypes;
import service.AbstractFavor;

import java.util.List;
import java.util.stream.Stream;

public class HotelInfo {
    private final Hotel hotel;

    public HotelInfo(Hotel hotel) {
        this.hotel = hotel;
    }

    /**
     * Метод формирует список всех комнат, отсортированных по убыванию звёзд.
     * @return Отфильтрованный список всех комнат.
     */
    public List<AbstractRoom> roomsByStars() {
        return hotel.getRooms()
                .stream()
                .toList()
                .reversed();
    }

    /**
     * Метод формирует список всех комнат, отсортированный по возрастанию цены.
     * @return Отфильтрованный список всех комнат.
     */
    public List<AbstractRoom> roomsByPrice() {
        return hotel.getRooms()
                .stream()
                .sorted(new RoomPriceComparator())
                .toList();
    }

    /**
     * Метод формирует список всех комнат, отсортировнный по возрастанию вместимости.
     * @return Отфильтрованный список всех комнат.
     */
    public List<AbstractRoom> roomsByCapacity() {
        return hotel.getRooms()
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
                .toList()
                .reversed();
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
    public long countAvailableRooms() {
        return filteredStreamAvailableRooms().count();
    }

    /**
     * Метод подсчитывает общее число клиентов.
     * @return Количество клиентов.
     */
    public int countClients() {
        return hotel.getClients().size();
    }

    /**
     * Метод подсчитывает цену конкретного номера или услуги.
     * @param favor Комната или услуга.
     * @return Стоимость аренды комнаты на день или разового оказания услуги.
     */
    public int getFavorPrice(AbstractFavor favor) {
        return favor.getPrice();
    }

    /**
     * Метод формирует список из 3-х последних клиентов комнаты.
     * @param room Комната, которую требуется проверить.
     * @return Список из 3-х последних клиентов.
     */
    public List<AbstractClient> getRoomClients(AbstractRoom room) {
        //TODO: Реализовать просмотр даты пребывания клиентов в комнате.
        return room.getVisitingClients()
                .stream()
                .limit(3)
                .toList();
    }

    /**
     * Метод формирует полную информацию о конкретной комнате.
     * @param room Комната.
     * @return Полная информация про комнату.
     */
    public String getRoomInfo(Room room) {
        return room.allInfo();
    }

    //TODO: Сформировать список постояльцев и их номеров (реализовать сортировку по алфавиту, дате освобождения номера).
    // Сформировать список, которые будут свободны по определённой дате в будущем. Посмотреть список услуг клиента и их
    // цену (сортировать по цене, дате). Нарисовать UML.

    /**
     * Служебный метод предназначен для устарнения дублирования кода фильтрованного стрима по свободным комнатам.
     * @return Стрим свободных комнат.
     */
    private Stream<AbstractRoom> filteredStreamAvailableRooms() {
        return hotel.getRooms().stream().filter(o -> o.getStatus() == RoomStatusTypes.AVAILABLE);
    }
}
