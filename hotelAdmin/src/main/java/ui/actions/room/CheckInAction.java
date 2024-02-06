package ui.actions.room;

import essence.person.AbstractClient;
import essence.room.AbstractRoom;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.InputHandler;
import ui.utils.ListToArrayConverter;
import ui.utils.exceptions.NoEntityException;

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
            String result = roomService.checkIn(room, clients) ? "Заселение прошло успешно" : "Заселить не удалось";
            System.out.println("\n" + result);
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
