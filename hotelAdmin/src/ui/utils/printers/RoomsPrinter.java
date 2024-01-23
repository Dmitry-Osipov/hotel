package ui.utils.printers;

import essence.room.AbstractRoom;
import ui.utils.ErrorMessages;

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
     */
    public static void printRooms(List<AbstractRoom> rooms) {
        if (rooms.isEmpty()) {
            System.out.println("\n" + ErrorMessages.NO_ROOMS.getMessage());
        } else {
            for (int i = 0; i < rooms.size(); i++) {
                System.out.println(i + 1 + ". " + rooms.get(i));
            }
        }
    }
}
