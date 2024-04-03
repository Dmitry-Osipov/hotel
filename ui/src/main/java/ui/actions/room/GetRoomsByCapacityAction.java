package ui.actions.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.RoomService;
import ui.actions.IAction;
import utils.exceptions.ErrorMessages;
import utils.exceptions.NoEntityException;
import utils.printers.RoomsPrinter;

import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по выводу списка всех комнат по возрастанию вместимости.
 */
@Component
public class GetRoomsByCapacityAction implements IAction {
    private final RoomService roomService;

    @Autowired
    public GetRoomsByCapacityAction(RoomService roomService) {
        this.roomService = roomService;
    }

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
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
