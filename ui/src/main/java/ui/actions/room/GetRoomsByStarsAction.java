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
 * Класс предоставляет логику выполнения действия по выводу списка всех комнат по убыванию звёзд.
 */
@Component
public class GetRoomsByStarsAction implements IAction {
    @Autowired
    private RoomService roomService;

    /**
     * Метод выполняет действие по выводу списка всех комнат по убыванию звёзд. При выполнении действия
     * выводится список всех комнат отеля, упорядоченный по убыванию звёзд. Если в отеле отсутствуют комнаты,
     * выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        try {
            System.out.println("\nСписок всех комнат по убыванию звёзд: ");
            RoomsPrinter.printRooms(roomService.roomsByStars());
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
