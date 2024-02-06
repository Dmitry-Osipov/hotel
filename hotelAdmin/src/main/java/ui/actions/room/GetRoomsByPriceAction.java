package ui.actions.room;

import service.RoomService;
import ui.actions.IAction;
import ui.utils.exceptions.NoEntityException;
import ui.utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка всех комнат по возрастанию цены.
 */
public class GetRoomsByPriceAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс предоставляет логику выполнения действия по выводу списка всех комнат по возрастанию цены.
     * @param roomService Класс обработки данных по комнатам.
     */
    public GetRoomsByPriceAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по выводу списка всех комнат по возрастанию цены. При выполнении действия
     * выводится список всех комнат отеля, упорядоченный по возрастанию цены. Если в отеле отсутствуют комнаты,
     * выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        try {
            System.out.println("\nСписок всех комнат по возрастанию цены: ");
            RoomsPrinter.printRooms(roomService.roomsByPrice());
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
