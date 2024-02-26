package ui.actions.room;

import essence.person.AbstractClient;
import service.ClientService;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.NoEntityException;
import utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка комнат клиента по убыванию времени выезда.
 */
public class GetClientRoomsByCheckOutTimeAction implements IAction {
    private final RoomService roomService;
    private final ClientService clientService;

    /**
     * Класс предоставляет логику выполнения действия по выводу списка комнат клиента по убыванию времени выезда.
     * @param roomService Класс обработки данных по комнатам.
     */
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
        }
    }
}
