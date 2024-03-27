package ui.actions.room;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.room.Room;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.exceptions.NoEntityException;

import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по выводу стоимости комнаты.
 */
@Component
public class GetRoomPriceAction implements IAction {
    @Autowired
    private RoomService roomService;

    /**
     * Метод выполняет действие по выводу стоимости комнаты. При выполнении действия выводится стоимость указанной
     * комнаты. Если в отеле отсутствуют комнаты, выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        try {
            Room room = (Room) InputHandler.getRoomByInput(roomService);
            System.out.println("\nСтоимость комнаты - " + roomService.getFavorPrice(room));
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
