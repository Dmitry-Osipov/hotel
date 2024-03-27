package ui.actions.room;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import service.RoomService;
import ui.actions.IAction;
import utils.exceptions.ErrorMessages;
import utils.exceptions.NoEntityException;
import utils.printers.RoomsPrinter;

import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по выводу списка свободных комнат в порядке возрастания цены.
 */
@Component
public class GetAvailableRoomsByPriceAction implements IAction {
    @Autowired
    private RoomService roomService;

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
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
