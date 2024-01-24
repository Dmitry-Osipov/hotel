package ui.actions.room;

import essence.person.AbstractClient;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;
import ui.utils.InputHandler;
import ui.utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка комнат клиента по возрастанию номера.
 */
public class GetClientRoomsByNumbersAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс предоставляет логику выполнения действия по выводу списка комнат клиента по возрастанию номера.
     * @param roomService Класс обработки данных по комнатам.
     */
    public GetClientRoomsByNumbersAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по выводу списка комнат клиента по возрастанию номера комнаты. При выполнении действия
     * выводится список комнат клиента, отсортированный по возрастанию номера комнат. Если в отеле нет клиентов,
     * выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        AbstractClient client = InputHandler.getClientByInput();
        if (client == null) {
            System.out.println("\n" + ErrorMessages.NO_CLIENTS.getMessage());
        } else {
            System.out.println("\nСписок всех комнат клиента по возрастанию номера: ");
            RoomsPrinter.printRooms(roomService.getClientRoomsByNumbers(client));
        }
    }
}
