package ui.actions.room;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.person.AbstractClient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.ClientService;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.NoEntityException;
import utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка комнат клиента по убыванию времени выезда.
 */
@Component
@Getter
@Setter
@NoArgsConstructor
public class GetClientRoomsByCheckOutTimeAction implements IAction {
    @Autowired
    private RoomService roomService;
    @Autowired
    private ClientService clientService;

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
