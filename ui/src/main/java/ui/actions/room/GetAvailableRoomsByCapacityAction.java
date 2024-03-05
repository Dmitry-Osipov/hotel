package ui.actions.room;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.RoomService;
import ui.actions.IAction;
import utils.exceptions.NoEntityException;
import utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка свободных комнат в порядке возрастания вместимости.
 */
@Component
@Getter
@Setter
@NoArgsConstructor
public class GetAvailableRoomsByCapacityAction implements IAction {
    @Autowired
    private RoomService roomService;

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
