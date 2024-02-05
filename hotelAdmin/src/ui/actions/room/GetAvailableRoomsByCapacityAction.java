package ui.actions.room;

import service.RoomService;
import ui.actions.IAction;
import ui.utils.exceptions.NoEntityException;
import ui.utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка свободных комнат в порядке возрастания вместимости.
 */
public class GetAvailableRoomsByCapacityAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс предоставляет логику выполнения действия по выводу списка свободных комнат в порядке возрастания
     * вместимости.
     * @param roomService Класс обработки данных по комнатам.
     */
    public GetAvailableRoomsByCapacityAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по выводу списка свободных комнат в порядке возрастания вместимости. При выполнении
     * действия выводится список свободных комнат, отсортированный по возрастанию вместимости. Если свободных комнат
     * нет, пользователю выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        try {
            System.out.println("\nСписок свободных комнат по возрастанию вместимости: ");
            RoomsPrinter.printRooms(roomService.availableRoomsByCapacity());
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
