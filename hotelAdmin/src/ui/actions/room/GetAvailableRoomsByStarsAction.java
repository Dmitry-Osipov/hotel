package ui.actions.room;

import service.RoomService;
import ui.actions.IAction;
import ui.utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка свободных комнат в порядке убывания звёзд.
 */
public class GetAvailableRoomsByStarsAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс предоставляет логику выполнения действия по выводу списка свободных комнат в порядке убывания звёзд.
     * @param roomService Класс обработки данных по комнатам.
     */
    public GetAvailableRoomsByStarsAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по выводу списка свободных комнат в порядке убывания звёзд. При выполнении
     * действия выводится список свободных комнат, отсортированный по убыванию звёзд. Если свободных комнат
     * нет, пользователю выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        System.out.println("\nСписок свободных комнат по убыванию звёзд: ");
        RoomsPrinter.printRooms(roomService.availableRoomsByStars());
    }
}
