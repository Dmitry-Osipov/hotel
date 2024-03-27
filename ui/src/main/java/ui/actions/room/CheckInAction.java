package ui.actions.room;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.person.AbstractClient;
import essence.room.AbstractRoom;
import service.ClientService;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.ListToArrayConverter;
import utils.exceptions.AccessDeniedException;
import utils.exceptions.ErrorMessages;
import utils.exceptions.InvalidDataException;
import utils.exceptions.NoEntityException;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс предоставляет логику выполнения действия по заселению клиентов в комнату.
 */
@Component
public class CheckInAction implements IAction {
    @Autowired
    private RoomService roomService;
    @Autowired
    private ClientService clientService;

    /**
     * Метод выполняет действие по заселению клиентов в комнату. При выполнении действия пользователю предлагается
     * выбрать комнату, затем выбрать клиентов, которых необходимо заселить в данную комнату. После валидации данных
     * происходит заселение выбранных клиентов в комнату. Пользователю выводится соответствующий результат операции.
     */
    @Override
    public void execute() {
        try {
            AbstractRoom room = InputHandler.getRoomByInput(roomService);
            List<AbstractClient> guests = InputHandler.getManyClientsByInput(clientService);
            AbstractClient[] clients = ListToArrayConverter.convertListToArray(guests, AbstractClient.class);
            roomService.checkIn(room, clients);
            System.out.println("\nЗаселение прошло успешно");
        } catch (NoEntityException | InvalidDataException | AccessDeniedException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
