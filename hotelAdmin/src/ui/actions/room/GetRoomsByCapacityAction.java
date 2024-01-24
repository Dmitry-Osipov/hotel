package ui.actions.room;

import service.RoomService;
import ui.actions.IAction;
import ui.utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка всех комнат по возрастанию вместимости.
 */
public class GetRoomsByCapacityAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс предоставляет логику выполнения действия по выводу списка всех комнат по возрастанию вместимости.
     * @param roomService Класс обработки данных по комнатам.
     */
    public GetRoomsByCapacityAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по выводу списка всех комнат по возрастанию вместимости. При выполнении действия
     * выводится список всех комнат отеля, упорядоченный по возрастанию вместимости. Если в отеле отсутствуют комнаты,
     * выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        System.out.println("\nСписок всех комнат по возрастанию вместимости: ");
        RoomsPrinter.printRooms(roomService.roomsByCapacity());
    }
}
