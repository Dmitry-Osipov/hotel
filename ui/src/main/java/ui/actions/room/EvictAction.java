package ui.actions.room;

import essence.person.AbstractClient;
import essence.room.AbstractRoom;
import service.ClientService;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.ListToArrayConverter;
import utils.exceptions.AccessDeniedException;
import utils.exceptions.InvalidDataException;
import utils.exceptions.NoEntityException;

import java.util.List;

/**
 * Класс предоставляет логику выполнения действия по выселению клиентов из комнаты.
 */
public class EvictAction implements IAction {
    private final RoomService roomService;
    private final ClientService clientService;

    /**
     * Класс предоставляет логику выполнения действия по выселению клиентов из комнаты.
     * @param roomService Класс обработки данных по комнатам.
     */
    public EvictAction(RoomService roomService, ClientService clientService) {
        this.roomService = roomService;
        this.clientService = clientService;
    }

    /**
     * Метод выполняет действие по выселению клиентов из комнаты. При выполнении действия пользователю предлагается
     * выбрать комнату, затем выбрать клиентов, которых необходимо выселить из данной комнаты. После валидации данных
     * происходит выселение выбранных клиентов из комнаты. Пользователю выводится соответствующий результат операции.
     */
    @Override
    public void execute() {
        try {
            AbstractRoom room = InputHandler.getRoomByInput(roomService);
            List<AbstractClient> guests = InputHandler.getManyClientsByInput(clientService);
            AbstractClient[] clients = ListToArrayConverter.convertListToArray(guests, AbstractClient.class);
            roomService.evict(room, clients);
            System.out.println("\nВыселение прошло успешно");
        } catch (NoEntityException | InvalidDataException | AccessDeniedException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
