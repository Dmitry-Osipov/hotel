package ui.actions.room;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import service.RoomService;
import ui.actions.IAction;
import utils.exceptions.NoEntityException;
import utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка свободных комнат в порядке возрастания вместимости.
 */
@Component
public class GetAvailableRoomsByCapacityAction implements IAction {
    @Autowired
    private RoomService roomService;

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
