package ui.actions.room;

import essence.person.AbstractClient;
import service.ClientService;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.NoEntityException;
import utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка комнат клиента по возрастанию номера.
 */
public class GetClientRoomsByNumbersAction implements IAction {
    private final RoomService roomService;
    private final ClientService clientService;

    /**
     * Класс предоставляет логику выполнения действия по выводу списка комнат клиента по возрастанию номера.
     * @param roomService Класс обработки данных по комнатам.
     */
    public GetClientRoomsByNumbersAction(RoomService roomService, ClientService clientService) {
        this.roomService = roomService;
        this.clientService = clientService;
    }

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
        }
    }
}
