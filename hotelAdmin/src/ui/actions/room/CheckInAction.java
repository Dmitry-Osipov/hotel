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
 * Класс предоставляет логику выполнения действия по заселению клиентов в комнату.
 */
public class CheckInAction implements IAction {
    /**
     * Метод выполняет действие по заселению клиентов в комнату. При выполнении действия пользователю предлагается
     * выбрать комнату, затем выбрать клиентов, которых необходимо заселить в данную комнату. После валидации данных
     * происходит заселение выбранных клиентов в комнату. Пользователю выводится соответствующий результат операции.
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
                        .checkIn(room, clients) ? "Заселение прошло успешно" : "Заселить не удалось";
            }
        } else {
            result = ErrorMessages.NO_ROOMS.getMessage();
        }
        System.out.println("\n" + result);
    }
}
