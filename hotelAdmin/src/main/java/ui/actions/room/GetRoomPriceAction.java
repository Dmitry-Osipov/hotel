package ui.actions.room;

import essence.room.Room;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.InputHandler;
import ui.utils.exceptions.NoEntityException;

/**
 * Класс предоставляет логику выполнения действия по выводу стоимости комнаты.
 */
public class GetRoomPriceAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс предоставляет логику выполнения действия по выводу стоимости комнаты.
     * @param roomService Класс обработки данных по комнатам.
     */
    public GetRoomPriceAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по выводу стоимости комнаты. При выполнении действия выводится стоимость указанной
     * комнаты. Если в отеле отсутствуют комнаты, выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        try {
            Room room = (Room) InputHandler.getRoomByInput();
            System.out.println("\nСтоимость комнаты - " + roomService.getFavorPrice(room));
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
