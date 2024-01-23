package ui.actions.room;

import essence.person.AbstractClient;
import essence.room.AbstractRoom;
import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;
import ui.utils.InputHandler;
import ui.utils.ListToArrayConverter;

import java.util.List;

/**
 * Класс предоставляет логику выполнения действия по выселению клиентов из комнаты.
 */
public class EvictAction implements IAction {
    /**
     * Метод выполняет действие по выселению клиентов из комнаты. При выполнении действия пользователю предлагается
     * выбрать комнату, затем выбрать клиентов, которых необходимо выселить из данной комнаты. После валидации данных
     * происходит выселение выбранных клиентов из комнаты. Пользователю выводится соответствующий результат операции.
     */
    @Override
    public void execute() {
        AbstractRoom room = InputHandler.getRoomByInput();

        String result;
        if (room != null) {
            List<AbstractClient> guests = InputHandler.getManyClientsByInput();
            if (guests.isEmpty()) {
                result = ErrorMessages.NO_CLIENTS.getMessage();
            } else {
                AbstractClient[] clients = ListToArrayConverter.convertListToArray(guests, AbstractClient.class);
                result = new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance())
                        .evict(room, clients) ? "Выселение прошло успешно" : "Выселить не удалось";
            }
        } else {
            result = ErrorMessages.NO_ROOMS.getMessage();
        }
        System.out.println("\n" + result);
    }
}
