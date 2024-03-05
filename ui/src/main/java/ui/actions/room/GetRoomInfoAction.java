package ui.actions.room;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.room.AbstractRoom;
import essence.room.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.NoEntityException;

/**
 * Класс предоставляет логику выполнения действия по выводу полной информации о комнате.
 */
@Component
@Getter
@Setter
@NoArgsConstructor
public class GetRoomInfoAction implements IAction {
    @Autowired
    private RoomService roomService;

    /**
     * Класс предоставляет логику выполнения действия по выводу полной информации о комнате.
     * @param roomService Класс обработки данных по комнатам.
     */
    public GetRoomInfoAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по выводу полной информации о комнате. При выполнении действия выводится полная
     * информация о выбранной комнате. Если комнат в отеле нет, выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        try {
            AbstractRoom room = InputHandler.getRoomByInput(roomService);
            System.out.println("\nПолная информация о комнате - " + roomService.getRoomInfo((Room) room));
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
