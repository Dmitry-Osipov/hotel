package ui.actions.room;

import essence.person.AbstractClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.ClientService;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.exceptions.NoEntityException;
import utils.printers.RoomsPrinter;

import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по выводу списка комнат клиента по убыванию времени выезда.
 */
@Component
public class GetClientRoomsByCheckOutTimeAction implements IAction {
    private final RoomService roomService;
    private final ClientService clientService;

    @Autowired
    public GetClientRoomsByCheckOutTimeAction(RoomService roomService, ClientService clientService) {
        this.roomService = roomService;
        this.clientService = clientService;
    }

    /**
     * Метод выполняет действие по выводу списка комнат клиента по убыванию времени выезда. При выполнении действия
     * выводится список комнат клиента, отсортированный по убыванию времени выезда. Если клиентов в отеле нет, выводится
     * соответствующее сообщение.
     */
    @Override
    public void execute() {
        try {
            AbstractClient client = InputHandler.getClientByInput(clientService);
            System.out.println("\nСписок всех комнат клиента по убыванию времени выезда: ");
            RoomsPrinter.printRooms(roomService.getClientRoomsByCheckOutTime(client));
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
