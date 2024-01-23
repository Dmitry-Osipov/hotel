package ui.actions.room;

import essence.room.Room;
import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;
import ui.utils.InputHandler;

/**
 * Класс предоставляет логику выполнения действия по выводу стоимости комнаты.
 */
public class GetRoomPriceAction implements IAction {
    /**
     * Метод выполняет действие по выводу стоимости комнаты. При выполнении действия выводится стоимость указанной
     * комнаты. Если в отеле отсутствуют комнаты, выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        Room room = (Room) InputHandler.getRoomByInput();

        if (room != null) {
            System.out.println("\nСтоимость комнаты - " +
                    new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance())
                            .getFavorPrice(room));
        } else {
            System.out.println("\n" + ErrorMessages.NO_ROOMS.getMessage());
        }
    }
}
