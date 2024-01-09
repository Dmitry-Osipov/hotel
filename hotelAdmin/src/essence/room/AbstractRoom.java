package essence.room;

import essence.person.AbstractClient;

import java.time.LocalDateTime;
import java.util.List;

public interface AbstractRoom {
    /**
     * Метод установки нового статуса команты.
     * @param status Новый статус.
     */
    void setStatus(RoomStatusTypes status);

    /**
     * Метод получения статуса комнаты.
     * @return Статус комнаты.
     */
    RoomStatusTypes getStatus();

    /**
     * Метод получения вместимости комнаты.
     * @return Вместимость комнаты.
     */
    int getCapacity();

    /**
     * Метод получения текущих клиентов комнаты.
     * @return Список клиентов.
     */
    List<AbstractClient> getClientsNowInRoom();

    /**
     * Метод добавления новых звёзд комнате.
     * @param stars Количество звёзд.
     */
    void setStars(int stars);

    /**
     * Метод получения количества звёзд комнаты.
     * @return Количество звёзд.
     */
    int getStars();

    /**
     * Метод устанавливает новую цену на комнату.
     * @param price Новая цена.
     */
    void setPrice(int price);

    /**
     * Метож получения цены комнаты.
     * @return Цена.
     */
    int getPrice();

    /**
     * Метод получения истории посещения комнаты.
     * @return Список клиентов, заезжавших в комнату.
     */
    List<AbstractClient> getVisitingClients();

    /**
     * Метод установки времени заселения в комнату.
     * @param checkInTime Время заселения.
     */
    void setCheckInTime(LocalDateTime checkInTime);

    /**
     * Метод получения времени заселения в комнату.
     * @return Время заселения.
     */
    LocalDateTime getCheckInTime();

    /**
     * Метод установки времени выселения из комнаты.
     * @param checkOutTime Время выселения.
     */
    void setCheckOutTime(LocalDateTime checkOutTime);

    /**
     * Метод получения времени выселения из комнаты.
     * @return Время выселения.
     */
    LocalDateTime getCheckOutTime();

    /**
     * Метод получения номера комнаты.
     * @return Номер комнаты.
     */
    int getNumber();
}
