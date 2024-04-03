package ui.actions.room;

import essence.person.AbstractClient;
import essence.room.AbstractRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.ClientService;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.ListToArrayConverter;
import utils.exceptions.AccessDeniedException;
import utils.exceptions.ErrorMessages;
import utils.exceptions.InvalidDataException;
import utils.exceptions.NoEntityException;
import utils.exceptions.TechnicalException;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс предоставляет логику выполнения действия по выселению клиентов из комнаты.
 */
@Component
public class EvictAction implements IAction {
    private final RoomService roomService;
    private final ClientService clientService;

    @Autowired
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
        } catch (NoEntityException | InvalidDataException | AccessDeniedException | TechnicalException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
