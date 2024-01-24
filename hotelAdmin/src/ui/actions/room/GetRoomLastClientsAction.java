package ui.actions.room;

import essence.person.AbstractClient;
import essence.room.AbstractRoom;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;
import ui.utils.InputHandler;

import java.util.List;


/**
 * Класс предоставляет логику выполнения действия по выводу последних клиентов комнаты.
 */
public class GetRoomLastClientsAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс предоставляет логику выполнения действия по выводу последних клиентов комнаты.
     * @param roomService Класс обработки данных по комнатам.
     */
    public GetRoomLastClientsAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по выводу последних клиентов комнаты. При выполнении действия выводится указанное
     * количество последних клиентов комнаты. Если комнат в отеле нет, выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        AbstractRoom room = InputHandler.getRoomByInput();

        if (room != null) {
            System.out.println("\nВведите количество последних клиентов: ");
            int count = InputHandler.getUserIntegerInput();

            List<AbstractClient> clients = roomService.getRoomLastClients(room, count);
            System.out.println("\nПоследние клиенты комнаты: ");
            for (int i = 0; i < clients.size(); i++) {
                System.out.println(i+1 + ". " + clients.get(i));
            }
        } else {
            System.out.println("\n" + ErrorMessages.NO_ROOMS.getMessage());
        }
    }
}
