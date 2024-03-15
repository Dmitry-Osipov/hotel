package ui.actions.room;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import service.RoomService;
import ui.actions.IAction;
import utils.exceptions.NoEntityException;
import utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка всех комнат по возрастанию цены.
 */
@Component
public class GetRoomsByPriceAction implements IAction {
    @Autowired
    private RoomService roomService;

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
