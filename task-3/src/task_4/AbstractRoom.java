package task_4;

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
}
