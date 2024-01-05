package room;

import person.AbstractClient;

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
}
