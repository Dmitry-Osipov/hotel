package ui.actions.room;

import essence.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    private final RoomService roomService;

    @Autowired
    public GetRoomPriceAction(RoomService roomService) {
        this.roomService = roomService;
    }

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
