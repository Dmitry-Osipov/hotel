package ui.actions.room;

import essence.person.AbstractClient;
import essence.room.AbstractRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.exceptions.NoEntityException;
import utils.file.PropertyFileReader;

import java.sql.SQLException;
import java.util.List;


/**
 * Класс предоставляет логику выполнения действия по выводу последних клиентов комнаты.
 */
@Component
public class GetRoomLastClientsAction implements IAction {
    private final RoomService roomService;

    @Autowired
    public GetRoomLastClientsAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по выводу последних клиентов комнаты. При выполнении действия выводится указанное
     * количество последних клиентов комнаты. Если комнат в отеле нет, выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        try {
            AbstractRoom room = InputHandler.getRoomByInput(roomService);
            int count = Integer.parseInt(PropertyFileReader.getValue("number_last_clients"));
            List<AbstractClient> clients = roomService.getRoomLastClients(room, count);
            System.out.println("\nПоследние клиенты комнаты: ");
            for (int i = 0; i < clients.size(); i++) {
                System.out.println(i+1 + ". " + clients.get(i));
            }
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
