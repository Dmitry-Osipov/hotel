package task4;

public interface AbstractClient {
    /**
     * Метод добавляет комнату в список посещаемых этим клиентом.
     * @param room Комната, которую посетил клиента.
     * @return true, если комнату удалось добавить, иначе false.
     */
    boolean addRoom(AbstractRoom room);
}
