package ui.actions.room;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.RoomService;
import ui.actions.IAction;
import utils.exceptions.NoEntityException;
import utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка свободных комнат в порядке убывания звёзд.
 */
@Component
@Getter
@Setter
@NoArgsConstructor
public class GetAvailableRoomsByStarsAction implements IAction {
    @Autowired
    private RoomService roomService;

    /**
     * Класс предоставляет логику выполнения действия по выводу списка свободных комнат в порядке убывания звёзд.
     * @param roomService Класс обработки данных по комнатам.
     */
    public GetAvailableRoomsByStarsAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по выводу списка свободных комнат в порядке убывания звёзд. При выполнении
     * действия выводится список свободных комнат, отсортированный по убыванию звёзд. Если свободных комнат
     * нет, пользователю выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        try {
            System.out.println("\nСписок свободных комнат по убыванию звёзд: ");
            RoomsPrinter.printRooms(roomService.availableRoomsByStars());
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
