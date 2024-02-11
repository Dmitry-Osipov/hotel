package ui.actions.room;

import service.RoomService;
import ui.actions.IAction;
import utils.exceptions.NoEntityException;
import utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка всех комнат по убыванию звёзд.
 */
public class GetRoomsByStarsAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс предоставляет логику выполнения действия по выводу списка всех комнат по убыванию звёзд.
     * @param roomService Класс обработки данных по комнатам.
     */
    public GetRoomsByStarsAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по выводу списка всех комнат по убыванию звёзд. При выполнении действия
     * выводится список всех комнат отеля, упорядоченный по убыванию звёзд. Если в отеле отсутствуют комнаты,
     * выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        try {
            System.out.println("\nСписок всех комнат по убыванию звёзд: ");
            RoomsPrinter.printRooms(roomService.roomsByStars());
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
