package ui.actions.room;

import essence.person.AbstractClient;
import essence.room.AbstractRoom;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.ListToArrayConverter;
import utils.exceptions.InvalidDataException;
import utils.exceptions.NoEntityException;

import java.util.List;

/**
 * Класс предоставляет логику выполнения действия по заселению клиентов в комнату.
 */
public class CheckInAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс предоставляет логику выполнения действия по заселению клиентов в комнату.
     * @param roomService Класс обработки данных по комнатам.
     */
    public CheckInAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по заселению клиентов в комнату. При выполнении действия пользователю предлагается
     * выбрать комнату, затем выбрать клиентов, которых необходимо заселить в данную комнату. После валидации данных
     * происходит заселение выбранных клиентов в комнату. Пользователю выводится соответствующий результат операции.
     */
    @Override
    public void execute() {
        try {
            AbstractRoom room = InputHandler.getRoomByInput();
            List<AbstractClient> guests = InputHandler.getManyClientsByInput();
            AbstractClient[] clients = ListToArrayConverter.convertListToArray(guests, AbstractClient.class);
            roomService.checkIn(room, clients);
            System.out.println("\nЗаселение прошло успешно");
        } catch (NoEntityException | InvalidDataException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
