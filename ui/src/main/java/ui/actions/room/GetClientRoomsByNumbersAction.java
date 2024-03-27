package ui.actions.room;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.person.AbstractClient;
import service.ClientService;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.exceptions.NoEntityException;
import utils.printers.RoomsPrinter;

import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по выводу списка комнат клиента по возрастанию номера.
 */
@Component
public class GetClientRoomsByNumbersAction implements IAction {
    @Autowired
    private RoomService roomService;
    @Autowired
    private ClientService clientService;

    /**
     * Метод выполняет действие по выводу списка комнат клиента по возрастанию номера комнаты. При выполнении действия
     * выводится список комнат клиента, отсортированный по возрастанию номера комнат. Если в отеле нет клиентов,
     * выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        try {
            AbstractClient client = InputHandler.getClientByInput(clientService);
            System.out.println("\nСписок всех комнат клиента по возрастанию номера: ");
            RoomsPrinter.printRooms(roomService.getClientRoomsByNumbers(client));
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
