package ui.actions.room;

import service.RoomService;
import ui.actions.IAction;
import ui.utils.exceptions.NoEntityException;
import ui.utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка свободных комнат в порядке возрастания цены.
 */
public class GetAvailableRoomsByPriceAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс предоставляет логику выполнения действия по выводу списка свободных комнат в порядке возрастания цены.
     * @param roomService Класс обработки данных по комнатам.
     */
    public GetAvailableRoomsByPriceAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по выводу списка свободных комнат в порядке возрастания цены. При выполнении
     * действия выводится список свободных комнат, отсортированный по возрастанию цены. Если свободных комнат
     * нет, пользователю выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        try {
            System.out.println("\nСписок свободных комнат по возрастанию цены: ");
            RoomsPrinter.printRooms(roomService.availableRoomsByPrice());
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
