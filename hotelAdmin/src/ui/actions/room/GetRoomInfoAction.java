package ui.actions.room;

import essence.room.AbstractRoom;
import essence.room.Room;
import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;
import ui.utils.InputHandler;

/**
 * Класс предоставляет логику выполнения действия по выводу полной информации о комнате.
 */
public class GetRoomInfoAction implements IAction {
    /**
     * Метод выполняет действие по выводу полной информации о комнате. При выполнении действия выводится полная
     * информация о выбранной комнате. Если комнат в отеле нет, выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        AbstractRoom room = InputHandler.getRoomByInput();
        if (room != null) {
            System.out.println("\nПолная информация о комнате - " +
                    new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance())
                            .getRoomInfo((Room) room));
        } else {
            System.out.println("\n" + ErrorMessages.NO_ROOMS.getMessage());
        }
    }
}
