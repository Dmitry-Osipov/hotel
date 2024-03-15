package ui.actions.room;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import service.RoomService;
import ui.actions.IAction;
import utils.exceptions.NoEntityException;
import utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка свободных комнат в порядке убывания звёзд.
 */
@Component
public class GetAvailableRoomsByStarsAction implements IAction {
    @Autowired
    private RoomService roomService;

    /**
     * Метод выполняет действие по выводу списка свободных комнат в порядке убывания звёзд. При выполнении
     * действия выводится список свободных комнат, отсортированный по убыванию звёзд. Если свободных комнат
     * нет, пользователю выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        try {
            System.out.println("\nСписок свободных комнат по убыванию звёзд: ");
            RoomsPrinter.printRooms(roomService.availableRoomsByStars());
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
