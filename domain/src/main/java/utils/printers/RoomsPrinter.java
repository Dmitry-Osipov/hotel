package utils.printers;

import essence.room.AbstractRoom;
import utils.exceptions.ErrorMessages;
import utils.exceptions.NoEntityException;

import java.util.List;

/**
 * Класс является финальным и предоставляет статический метод для вывода списка комнат в консоль или сообщения об
 * отсутствии комнат.
 */
public final class RoomsPrinter {
    private RoomsPrinter() {
    }

    /**
     * Метод выводит в консоль список комнат или сообщение об отсутствии комнат.
     * @param rooms Список комнат.
     * @throws NoEntityException Ошибка связана с отсутствием комнат.
     */
    public static void printRooms(List<AbstractRoom> rooms) throws NoEntityException {
        if (rooms.isEmpty()) {
            throw new NoEntityException(ErrorMessages.NO_ROOMS.getMessage());
        } else {
            for (int i = 0; i < rooms.size(); i++) {
                System.out.println(i+1 + ". " + rooms.get(i));
            }
        }
    }
}
