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
 * Класс предоставляет логику выполнения действия по выводу списка всех комнат по возрастанию вместимости.
 */
@Component
@Getter
@Setter
@NoArgsConstructor
public class GetRoomsByCapacityAction implements IAction {
    @Autowired
    private RoomService roomService;

    /**
     * Метод выполняет действие по выводу списка всех комнат по возрастанию вместимости. При выполнении действия
     * выводится список всех комнат отеля, упорядоченный по возрастанию вместимости. Если в отеле отсутствуют комнаты,
     * выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        try {
            System.out.println("\nСписок всех комнат по возрастанию вместимости: ");
            RoomsPrinter.printRooms(roomService.roomsByCapacity());
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
